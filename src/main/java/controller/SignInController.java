package controller;

import dao.CardDao;
import dao.UserDao;
import entity.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class SignInController {
    private static final Logger LOGGER = Logger.getLogger(SignInController.class.getName());
    private static final String HOME_GUI_FXML_PATH = "/initHomeGui.fxml";
    private static final String LOGIN_FAILED_PATH = "ELEM_login_failed";
    UserDao ud;
    CardDao cd;
    CreateAccountController cac;
    private Stage stage;
    private Scene scene;
    private User currentUser;
    final ChoiceBox<String> cb = new ChoiceBox<>(
            FXCollections.observableArrayList("English", "Suomeksi", "日本語で", "한국어")
    );
    Locale locale = Locale.getDefault();

    @FXML
    private TextField tUsername;
    @FXML
    private PasswordField tPassw;
    @FXML
    private Button tOk;
    @FXML
    private TextField sUsername;
    @FXML
    private PasswordField sPassw;
    @FXML
    private Button sOk;
    @FXML
    private Hyperlink createAccount;
    @FXML
    private Label nameTag;
    @FXML
    public ChoiceBox<String> languageChoice;
    private boolean initializingLanguageChoice;

    public String gettUsername() {
        return tUsername.getText();
    }

    public String gettPassw() {
        return tPassw.getText();
    }

    public String getSUsername() {
        return sUsername.getText();
    }

    public String getsPassw() {
        return sPassw.getText();
    }

    public void setCurrentUser(User cu) {
        currentUser = cu;
    }


    public void initialize() {
        if (languageChoice == null) {
            return;
        }
        initializingLanguageChoice = true;
        try {
            languageChoice.setItems(cb.getItems());
            languageChoice.getSelectionModel().select(languageIndexForLocale(
                    LanguageManager.getLocale()
            ));
        } finally {
            initializingLanguageChoice = false;
        }
    }

    private int languageIndexForLocale(Locale selectedLocale) {
        return switch (selectedLocale.getLanguage()) {
            case "fi" -> 1;
            case "ja" -> 2;
            case "ko" -> 3;
            default -> 0;
        };
    }

    public void tLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == tOk) {
            String un = gettUsername();
            String ps = gettPassw();
            String email = "teacher@email.fi";
            User teacher = new User(un, email, ps, User.Role.teacher);
            if (UserDao.logInUser(teacher)) {
                UserDao.logInUser(teacher);
                setCurrentUser(teacher);
                FXMLLoader fxmlLoader = new FXMLLoader(
                        getClass().getResource(HOME_GUI_FXML_PATH),
                        LanguageManager.getBundle()
                );
                Parent root = fxmlLoader.load();
                SignInController controller = fxmlLoader.getController();
                controller.setUserGreeting(currentUser.getUserName());
                controller.setCurrentUser(currentUser);
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(LanguageManager.getBundle().getString(LOGIN_FAILED_PATH));
                alert.setHeaderText(LanguageManager.getBundle().getString(LOGIN_FAILED_PATH));
                alert.setContentText(LanguageManager.getBundle().getString("ELEM_wrong"));
                alert.showAndWait();
            }


        }
    }

    public void sLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == sOk) {
            String un = getSUsername();
            String ps = getsPassw();
            String email = "student@email.fi";
            User student = new User(un, email, ps, User.Role.student);
            if (UserDao.logInUser(student)) {
                UserDao.logInUser(student);
                setCurrentUser(student);
                FXMLLoader fxmlLoader = new FXMLLoader(
                        getClass().getResource(HOME_GUI_FXML_PATH),
                        LanguageManager.getBundle()
                );
                Parent root = fxmlLoader.load();
                SignInController controller = fxmlLoader.getController();
                controller.setUserGreeting(currentUser.getUserName());
                controller.setCurrentUser(currentUser);
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(LanguageManager.getBundle().getString(LOGIN_FAILED_PATH));
                alert.setHeaderText(LanguageManager.getBundle().getString(LOGIN_FAILED_PATH));
                alert.setContentText(LanguageManager.getBundle().getString("ELEM_wrong"));
                alert.showAndWait();
            }

        }
    }

    public void switchToCreateAccount(javafx.event.ActionEvent actionEvent) throws IOException {
        Locale localeCA = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createAccount.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        cac = fxmlLoader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreateCards(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        Locale localeCC = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createCard.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        CcController cardCtrl = fxmlLoader.getController();
        cardCtrl.setUser(currentUser);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLibrary(MouseEvent actionEvent) throws IOException {
        Locale localeL = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/libraryUI.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        LibraryController lc = fxmlLoader.getController();
        lc.setUser(currentUser);
        lc.getAllCards();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreateQuiz(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        Locale localeCQ = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createQuizUI.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToStudyMaterials(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        Locale localeSM = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createAccount.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToProfile(javafx.scene.input.MouseEvent actionEvent) throws IOException {
        Locale localeP = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(HOME_GUI_FXML_PATH), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();

        SignInController controller = fxmlLoader.getController();
        controller.setUserGreeting(currentUser.getUserName());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setUserGreeting(String un) {
        nameTag.setText(un);
    }

    public int getUserId() {
        return currentUser.getUserId();
    }

    public void changeLanguage(ActionEvent e) throws IOException {
        if (initializingLanguageChoice) {
            return;
        }

        int lang = languageChoice.getSelectionModel().getSelectedIndex();
        switch (lang) {
            case 0:
                LanguageManager.setLocale(new Locale("en", "EN"));
                break;
            case 1:
                LanguageManager.setLocale(new Locale("fi", "FI"));
                break;
            case 2:
                LanguageManager.setLocale(new Locale("ja", "JP"));
                break;
            case 3:
                LanguageManager.setLocale(new Locale("ko", "KR"));
                break;
            default:
                LOGGER.info("Invalid language choice, defaulting to English");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loginUi.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
