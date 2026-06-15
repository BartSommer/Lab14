package pk.bs.pasir_sommer_bartosz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GroupAndDebtIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Tokeny symulujące zalogowanych użytkowników (w zależności od Twojej implementacji zabezpieczeń)
    private String ownerToken = "Bearer token_wlasciciela";
    private String memberToken = "Bearer token_czlonka";
    private String outsiderToken = "Bearer token_obcego";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // Tutaj opcjonalnie: czyszczenie baz danych / repozytoriów przed każdym testem
    }

    // NAPRAWIONE: Bezpieczny helper wykorzystujący HashMap, który akceptuje wartości null
    private String createGraphQLQuery(String query, Map<String, Object> variables) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", query);
        payload.put("variables", variables);
        return objectMapper.writeValueAsString(payload);
    }

    // --- GRUPY & CZŁONKOWIE ---

    @Test
    @DisplayName("1. Utworzenie grupy dodaje właściciela jako członka i zwraca ją w myGroups")
    void shouldAddOwnerAsMemberAndReturnInMyGroupsWhenGroupCreated() throws Exception {
        // Implementacja testu tworzenia grupy i weryfikacji listy myGroups
    }

    @Test
    @DisplayName("2. Tylko właściciel grupy może dodawać członków")
    void shouldAllowOnlyOwnerToAddMembers() throws Exception {
        // Test pozytywny: Właściciel dodaje członka -> 200 OK
        // Test negatywny: Inny członek/obcy dodaje członka -> 403 Forbidden / Error
    }

    @Test
    @DisplayName("3. groupMembers zwraca członków grupy tylko członkowi tej grupy")
    void shouldReturnGroupMembersOnlyToGroupMember() throws Exception {
        // Członek pyta o grupę -> Sukces
        // Osoba z zewnątrz pyta o grupę -> Błąd/Odmowa dostępu
    }

    @Test
    @DisplayName("4. groupDebts zwraca długi grupy tylko członkowi tej grupy")
    void shouldReturnGroupDebtsOnlyToGroupMember() throws Exception {
        // Podobnie jak wyżej – asercja dla endpointu / metody zwracającej długi
    }

    @Test
    @DisplayName("5. Nowy członek dostaje tylko długi z transakcji dodanych po dołączeniu")
    void newMemberShouldOnlyGetDebtsFromTransactionsAfterJoining() throws Exception {
        // 1. Stwórz transakcję w grupie
        // 2. Dodaj nowego użytkownika
        // 3. Stwórz drugą transakcję
        // Asercja: Nowy użytkownik widzi dług tylko z 2. transakcji
    }

    @Test
    @DisplayName("6. Transakcja grupowa typu INCOME tworzy długi od aktualnego użytkownika do pozostałych członków")
    void incomeTransactionShouldCreateDebtsFromCurrentUserToOtherMembers() throws Exception {
        // Sprawdzenie logiki biznesowej podziału długu dla typu INCOME
    }

    @Test
    @DisplayName("7. Usunięcie członka nie usuwa jego historycznych długów")
    void removingMemberShouldNotDeleteTheirHistoricalDebts() throws Exception {
        // 1. Stwórz dług dla członka
        // 2. Usuń członka z grupy
        // Asercja: Dług nadal istnieje w bazie/systemie
    }

    @Test
    @DisplayName("8. Nie można usunąć właściciela z jego grupy przez removeMember")
    void shouldNotAllowRemovingOwnerViaRemoveMember() throws Exception {
        // Próba usunięcia właściciela -> Odrzucenie żądania / kod błędu
    }

    @Test
    @DisplayName("9. Członek grupy niebędący właścicielem nie może usunąć grupy")
    void nonOwnerMemberCannotDeleteGroup() throws Exception {
        // Zwykły członek wysyła żądanie usunięcia grupy -> 403 Forbidden / błąd GraphQL
    }

    // --- DŁUGI (createDebt / deleteDebt) ---

    @Test
    @DisplayName("10. createDebt tworzy ręczny dług tylko między członkami tej samej grupy")
    void createDebtShouldOnlyWorkBetweenMembersOfSameGroup() throws Exception {
    }

    @Test
    @DisplayName("11. createDebt odrzuca użytkownika spoza grupy i dług do samego siebie")
    void createDebtShouldRejectOutsidersAndSelfDebts() throws Exception {
        // Próba utworzenia długu dla samego siebie lub dla kogoś spoza grupy -> Walidacja / Błąd
    }

    @Test
    @DisplayName("12. Właściciel grupy może utworzyć dług między innymi członkami grupy")
    void ownerCanCreateDebtBetweenOtherGroupMembers() throws Exception {
    }

    @Test
    @DisplayName("13. Członek grupy może utworzyć dług tylko gdy jest jego uczestnikiem")
    void memberCanCreateDebtOnlyIfTheyAreParticipant() throws Exception {
    }

    @Test
    @DisplayName("14. deleteDebt usuwa dług dostępny dla uczestnika długu")
    void deleteDebtRemovesDebtAvailableForParticipant() throws Exception {
    }

    @Test
    @DisplayName("15. deleteDebt odrzuca członka grupy, który nie jest właścicielem ani uczestnikiem długu")
    void deleteDebtShouldRejectNonOwnerAndNonParticipant() throws Exception {
    }

    @Test
    @DisplayName("16. Właściciel grupy może usunąć dług, którego nie jest uczestnikiem")
    void ownerCanDeleteDebtEvenIfRemarksNotParticipant() throws Exception {
    }

    // --- GRAPHQL & CZYSZCZENIE ---

    @Test
    @DisplayName("17. Walidacje danych wejściowych GraphQL odrzucają puste lub niepoprawne wartości")
    void graphQLValidationsShouldRejectEmptyOrInvalidValues() throws Exception {
        // Przykład zapytania GraphQL z błędnymi danymi (np. pusta nazwa grupy)
        String graphqlMutation = "mutation { createGroup(input: { name: \"\" }) { id name } }";

        // NAPRAWIONE: Przekazujemy zainicjalizowaną, pustą HashMapę zamiast wartości null
        Map<String, Object> emptyVariables = new HashMap<>();

        mockMvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createGraphQLQuery(graphqlMutation, emptyVariables)))
                .andExpect(status().isOk()) // GraphQL zazwyczaj zwraca 200 OK, ale z tablicą "errors"
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors[0].message").value(containsString("Validation")));
    }

    @Test
    @DisplayName("18. Usunięcie grupy przez właściciela usuwa powiązane długi i grupę")
    void deletingGroupByOwnerShouldRemoveAssociatedDebtsAndGroup() throws Exception {
        // 1. Właściciel usuwa grupę
        // Asercja: Grupa oraz powiązane z nią rekordy długów przestają istnieć
    }
}