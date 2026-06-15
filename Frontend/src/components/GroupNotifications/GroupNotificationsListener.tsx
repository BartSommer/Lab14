import { useEffect } from "react";
import { toast } from "react-toastify";
import { useAuth } from "../../context/AuthContext";

interface GroupNotification {
  type: "GROUP_EXPENSE_ADDED";
  groupId: number | string;
  groupName: string;
  title: string;
  amount: number;
  userShare: number;
  createdByEmail: string;
  message: string;
}

const getWebSocketUrl = (token: string) => {
  const protocol = window.location.protocol === "https:" ? "wss" : "ws";
  // Oczyszczamy dodatkowo token z ewentualnych znaków sterujących nowej linii
  const cleanToken = token.replace(/[\r\n]/g, "");
  return `${protocol}://localhost:8080/ws/group-notifications?token=${encodeURIComponent(cleanToken)}`;
};

const GroupNotificationsListener = () => {
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    if (!isAuthenticated) return;

    const token = localStorage.getItem("accessToken");
    if (!token) return;

    // POPRAWKA DLA TS I SONARA: Bezpieczne i uniwersalne wyrażenie regularne dla formatu JWT
    const jwtPattern = /^[a-zA-Z0-9\-_]+\.[a-zA-Z0-9\-_]+\.[a-zA-Z0-9\-_]+$/;
    if (!jwtPattern.test(token)) {
      console.error("Nieprawidłowy format tokenu uwierzytelniającego.");
      return;
    }

    const socket = new WebSocket(getWebSocketUrl(token));

    socket.onmessage = (event) => {
      try {
        const notification = JSON.parse(event.data) as GroupNotification;
        if (notification.type === "GROUP_EXPENSE_ADDED") {
          toast.info(notification.message);
        }
      } catch (error) {
        console.error("Nie udało się obsłużyć komunikatu grupowego:", error);
      }
    };

    socket.onerror = (error) => {
      console.error("Błąd połączenia WebSocket z komunikatami grupowymi:", error);
    };

    return () => {
      socket.close();
    };
  }, [isAuthenticated]);

  return null;
};

export default GroupNotificationsListener;