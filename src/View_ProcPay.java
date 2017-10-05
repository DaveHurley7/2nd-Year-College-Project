import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

public class View_ProcPay {

    private Scene scene;
    private TabPane tabs;
    private Tab procTab;
    private Tab payTab;
    private ListView<String> procList, payList;
    private String title = "Payments and Prodecures - ";
    private int pIndex, iIndex;
    private MenuItem editProc, removeProc;

    public View_ProcPay(Controller c){
        BorderPane main = new BorderPane();
        tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        procTab = new Tab("Procedures");
        payTab = new Tab("Payments");
        scene = new Scene(main,700,400);
        tabs.setTabMinWidth(scene.getWidth()/2-scene.getWidth()/20);

        MenuBar bar = new MenuBar();
        Menu backFile = new Menu("File");
        MenuItem back = new MenuItem("Back");

        Menu procMenu = new Menu("Procedure");
        MenuItem addProc = new MenuItem("Add");
        editProc = new MenuItem("Edit");
        editProc.setDisable(true);
        removeProc = new MenuItem("Remove");
        removeProc.setDisable(true);

        Menu payMenu = new Menu("Payment");
        MenuItem addPay = new MenuItem("Add");

        backFile.getItems().add(back);
        procMenu.getItems().addAll(addProc,editProc,removeProc);
        payMenu.getItems().add(addPay);
        bar.getMenus().addAll(backFile,procMenu,payMenu);

        addProc.setOnAction(e -> c.addProcedure(pIndex,iIndex));
        editProc.setOnAction(e -> c.editProcedure(pIndex,iIndex));
        removeProc.setOnAction(e -> c.removeProcedure(pIndex,iIndex));

        addPay.setOnAction(e -> c.addPayment(pIndex,iIndex));

        back.setOnAction(e -> c.goToPatientView());

        procList = new ListView<>();
        payList = new ListView<>();
        procList.setOnMouseClicked(e -> c.enableEditAndRemove());

        procTab.setContent(procList);
        payTab.setContent(payList);

        tabs.getTabs().addAll(procTab,payTab);

        main.setTop(bar);
        main.setCenter(tabs);
    }

    public Scene getScene(){
        return scene;
    }

    public String getTitle(){
        return title;
    }

    public TabPane getTabPane() {
        return tabs;
    }

    public Tab getProcTab(){
        return procTab;
    }

    public Tab getPayTab(){
        return payTab;
    }

    public ListView<String> getProcList(){
        return procList;
    }

    public ListView<String> getPayList(){
        return payList;
    }

    public void setRootIndexes(int pIn, int iIn){
        pIndex = pIn;
        iIndex = iIn;
    }

    public MenuItem getEditProc(){
        return editProc;
    }

    public MenuItem getRemoveProc(){
        return removeProc;
    }

}