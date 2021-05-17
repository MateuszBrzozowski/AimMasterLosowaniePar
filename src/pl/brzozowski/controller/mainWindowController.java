package pl.brzozowski.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.brzozowski.model.DrawModel;
import pl.brzozowski.model.Pair;
import pl.brzozowski.model.Player;

public class mainWindowController {

    protected final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private DrawModel drawModel;
    @FXML
    private TableView<Player> table_AllPlayers;
    @FXML
    private TableColumn<Player,String> table_AllPlayersNick;
    @FXML
    private TableView<Player> table_PresentPlayers;
    @FXML
    private TableColumn<Player,String>  table_PresentPlayersNick;
    @FXML
    private TableView<Pair> tablePair;
    @FXML
    private TableColumn<Pair,String> tablePair_Player1;
    @FXML
    private TableColumn<Pair,String> tablePair_Player2;

    @FXML
    public void initialize(){
        drawModel =  new DrawModel();
        fillTablePlayer();
    }

    private void fillTablePlayer() {
        ObservableList<Player> players = FXCollections.observableArrayList();
        players = drawModel.getAllPlayers();

        table_AllPlayers.setItems(players);
        this.table_AllPlayersNick.setCellValueFactory(playerStringCellDataFeatures -> playerStringCellDataFeatures.getValue().nickProperty());
    }

    @FXML
    public void table_AllPlayers_Clicked(MouseEvent event) {
        Player player = table_AllPlayers.getSelectionModel().getSelectedItem();
        drawModel.setPlayerSelected(player);
    }

    @FXML
    public void toRight_Clicked(MouseEvent event) {
        if (drawModel.getPlayerSelected().getNick()!=null){
            Player player = drawModel.getPlayerSelected();
            logger.info(player.getNick() + " KD : " + player.getKd());
            boolean isPlayer = false;
            for (int i = 0; i < drawModel.getPresentPlayers().size(); i++) {
                if(drawModel.getPresentPlayers().get(i).getNick().equals(player.getNick())){
                    isPlayer =true;
                }
            }
            if (!isPlayer){
                drawModel.getPresentPlayers().add(player);
                table_PresentPlayers.setItems(drawModel.getPresentPlayers());
                this.table_PresentPlayersNick.setCellValueFactory(playerStringCellDataFeatures -> playerStringCellDataFeatures.getValue().nickProperty());
            }else {
                logger.info("Wybrany gracz już jest dodany");
            }
        }else {
            logger.error("Nie wybrano osoby");
        }

    }

    @FXML
    public void toLeft_Clicked(MouseEvent event) {
        if (drawModel.getPlayerSelectedPresent().getNick()!=null){
            Player player = drawModel.getPlayerSelectedPresent();
            for (int i = 0; i < drawModel.getPresentPlayers().size(); i++) {
                if (drawModel.getPresentPlayers().get(i).getNick()==player.getNick()){
                    drawModel.getPresentPlayers().remove(i);
                }
            }
            table_PresentPlayers.setItems(drawModel.getPresentPlayers());
        }else {
            logger.error("Nie wybrano osby do usunięcia");
        }
    }

    @FXML
    public void table_PresentPlayers_Clicked(MouseEvent event) {
        Player player = table_PresentPlayers.getSelectionModel().getSelectedItem();
        drawModel.setPlayerSelectedPresent(player);
    }

    @FXML
    public void buttonLosujPary_Clicked(MouseEvent event) {
        drawModel.draft();
        //TODO Uzueplnić tabele wylosowanymi parami
        ObservableList<Pair> pairs = FXCollections.observableArrayList();
        pairs = drawModel.getPairsOfPlayers();

        tablePair.setItems(pairs);
        this.tablePair_Player1.setCellValueFactory(pairStringCellDataFeatures -> pairStringCellDataFeatures.getValue().getPlayer1().nickProperty());
        this.tablePair_Player2.setCellValueFactory(pairStringCellDataFeatures -> pairStringCellDataFeatures.getValue().getPlayer2().nickProperty());
    }
}
