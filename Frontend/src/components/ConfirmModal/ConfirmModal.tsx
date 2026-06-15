import type { ReactNode } from "react";
import styles from "./ConfirmModal.module.scss";

interface ConfirmModalProps {
  visible: boolean;
  title?: string;
  message: ReactNode;
  confirmLabel?: string;
  cancelLabel?: string;
  onConfirm: () => void;
  onCancel: () => void;
}

const ConfirmModal = ({
                        visible,
                        title = "Potwierdź akcję",
                        message,
                        confirmLabel = "Potwierdź",
                        cancelLabel = "Anuluj",
                        onConfirm,
                        onCancel,
                      }: ConfirmModalProps) => {
  if (!visible) return null;

  return (
      /* NAPRAWA DLA SONARA: Zamiast div z onClick i osobnym div z role="dialog",
        używamy natywnego, semantycznego tagu <dialog>. Tło obsługujemy jako semantyczny przycisk zamykający.
      */
      <dialog open className={styles.modalOverlay}>
        <button
            type="button"
            className={styles.overlayCloseButton}
            aria-label="Zamknij okno"
            onClick={onCancel}
        />
        <div className={styles.modal}>
          <h3 id="confirm-modal-title">{title}</h3>
          <p>{message}</p>
          <div className={styles.actions}>
            <button type="button" className={styles.cancel} onClick={onCancel}>
              {cancelLabel}
            </button>
            <button type="button" className={styles.confirm} onClick={onConfirm}>
              {confirmLabel}
            </button>
          </div>
        </div>
      </dialog>
  );
};

export default ConfirmModal;