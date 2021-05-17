package pl.brzozowski.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class DrawModel {

    protected final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private List<String> playerName = new LinkedList<>();
    private List<Float> playerKD = new LinkedList<>();
    private ObservableList<Player> players = FXCollections.observableArrayList();
    private ObservableList<Player> presentPlayers = FXCollections.observableArrayList();
    private ObservableList<Pair> pairsOfPlayers = FXCollections.observableArrayList();
    private ObjectProperty<Player> playerSelected = new SimpleObjectProperty<>(new Player());
    private ObjectProperty<Player> playerSelectedPresent = new SimpleObjectProperty<>(new Player());
    private List<Player> aboveKDPlayers = new LinkedList<>();
    private List<Player> belowKDPlayers = new LinkedList<>();
    private final String PATH_PLAYER ="Z:\\PROJECTS_JAVA\\037_RangersPL_AimMaster_Pary\\src\\pl\\brzozowski\\files\\players.txt";
    private final String PATH_KD = "Z:\\PROJECTS_JAVA\\037_RangersPL_AimMaster_Pary\\src\\pl\\brzozowski\\files\\kdPlayers.txt";

    public DrawModel() {
        getPlayersNameFromFile();
        getPlayerKDFromFile();
        connectPlayerWithKD();
    }

    private void connectPlayerWithKD() {
        if (playerName.size()==playerKD.size()){
            for (int i = 0; i < playerName.size(); i++) {
                if (playerKD.get(i)!=-1){
                    Player player = new Player(playerName.get(i),playerKD.get(i));
                    players.add(player);
                    logger.info("{} KD: {}",player.getNick(),player.getKd());
                }
            }
            logger.info("Gracze wczytani prawidlowo.");
        }else {
            //TODO można zaimplementować żeby się to wyświetliło nad tabelami. Narazie zakładamy że się nie pojebaliśmy.
            logger.error("Pliki z liczbą graczy i KD są nie równe. Program może działać nie prawidłowo.");
        }

    }

    /**
     * Pobiera KD graczy z pliku tekstowego
     */
    private void getPlayerKDFromFile() {
        try {
            FileReader fileReader = new FileReader(PATH_KD);
            Scanner scanner = new Scanner(fileReader);
            while (scanner.hasNextLine()){
                String buffLine = scanner.nextLine();
                buffLine = buffLine.replace(',','.');
                buffLine = buffLine.replaceAll("\\s","");
                float buffFloatLine;
                try {
                    buffFloatLine = Float.parseFloat(buffLine);
                } catch (NumberFormatException e){
                    buffFloatLine = -1;

                }
                playerKD.add(buffFloatLine);
            }
        } catch (FileNotFoundException e) {
            logger.error("Nie znaleziono plik. {}",e);
        }
    }

    /**
     * Pobiera nazwy graczy z pliku tekstowego
     */
    private void getPlayersNameFromFile() {
        try {
            FileReader fileReader = new FileReader(PATH_PLAYER);
            Scanner scanner = new Scanner(fileReader);
            while (scanner.hasNextLine()){
                String bufferLine = scanner.nextLine();
                playerName.add(bufferLine);
            }
        } catch (FileNotFoundException e) {
            logger.error("Nie znaleziono plik. {}",e);
        }
    }

    public void draft() {
        pairsOfPlayers.clear();
        //Jeśli wybranych graczy jest liczba parzysta
        if (presentPlayers.size()%2==0){
            //średnia kd z obecnych graczy
            float avg;
            float sum =0;
            for (Player player: presentPlayers){
                sum += player.getKd();
            }
            avg=sum/presentPlayers.size();
            //tworzymy liste graczy powyzej i ponizej sredniego kd oraz szukamy najwyzszego i najmniejszego KD
            float minKD = presentPlayers.get(0).getKd();
            float maxKD = presentPlayers.get(0).getKd();
            for (Player player: presentPlayers){
                if (player.getKd()>avg){
                    aboveKDPlayers.add(player);
                    logger.info(player.getNick() + " KD: " + player.getKd() + " Do powyżej");
                }else {
                    belowKDPlayers.add(player);
                    logger.info(player.getNick() + " KD: " + player.getKd() + " Do poniżej");
                }
                if (player.getKd()>maxKD){
                    maxKD=player.getKd();
                }
                if (player.getKd()<minKD){
                    minKD = player.getKd();
                }
            }

//            listy musza być równe - musimy do tego dążyć
            while (aboveKDPlayers.size()!=belowKDPlayers.size()){
                logger.info("Koszyki nie są równe.");
                if (aboveKDPlayers.size()>belowKDPlayers.size()){
                    //szukanie najmniejszego KD w liscie powyzej sredniego KD
                    logger.info("Przenoszę z listy powyżej do listy poniżej średniego KD");
                    float minInAbove = maxKD;
                    int indexMinInAbove = 0;
                    float maxInBelow = minKD;
                    int indexMaxInBelow = 0;
                    for (int i =0; i< aboveKDPlayers.size() ; i++){
                        if (aboveKDPlayers.get(i).getKd()<=minInAbove){
                            minInAbove = aboveKDPlayers.get(i).getKd();
                            indexMinInAbove = i;
                        }
                    }
                    belowKDPlayers.add(aboveKDPlayers.get(indexMinInAbove));
                    aboveKDPlayers.remove(indexMinInAbove);
                }else if (belowKDPlayers.size()>aboveKDPlayers.size()){
                    //szukanie najwiekszego KD w liscie ponizej sredniego KD
                    logger.info("Przenoszę z listy poniżej do listy powyżej średniego KD");
                    float maxInBelow = minKD;
                    int indexMaxInBelow = 0;
                    for (int i =0; i< belowKDPlayers.size() ; i++){
                        if (belowKDPlayers.get(i).getKd()>=maxInBelow){
                            maxInBelow = belowKDPlayers.get(i).getKd();
                            indexMaxInBelow = i;
                        }
                    }
                    aboveKDPlayers.add(belowKDPlayers.get(indexMaxInBelow));
                    belowKDPlayers.remove(indexMaxInBelow);

                }else {
                    logger.info("Listy są równe. Jest w pyte. Lecimy dalej.");
                }
            }
        }else {
            logger.error("Liczba graczy nie parzysta");
        }

        //Losowanie gracza 1 z listy powyzej sredniej i losowanie dla Niego gracza 2 z listy ponizej sredniej.
        int numberOfDraw = aboveKDPlayers.size();
        for (int i = 0; i < numberOfDraw; i++) {
            Pair pair = new Pair();
            Random random = new Random();
            int indexOfPlayer1 = random.nextInt(aboveKDPlayers.size());
            int indexOfPlayer2 = random.nextInt(belowKDPlayers.size());
            Player player1 = new Player();
            Player player2 = new Player();
            player1 = aboveKDPlayers.get(indexOfPlayer1);
            player2 = belowKDPlayers.get(indexOfPlayer2);
            aboveKDPlayers.remove(indexOfPlayer1);
            belowKDPlayers.remove(indexOfPlayer2);
            logger.info("Gracz 1 : {} - Gracz 2 : {}",player1.getNick(),player2.getNick());
            pair.setPlayer1(player1);
            pair.setPlayer2(player2);
            pairsOfPlayers.add(pair);
        }
        //TODO Zapisać pary do pliku tak zeby latwo mozna bylo przekopiować na TS czy tam gdziekolwiek.
        writePairsToFile();

    }

    private void writePairsToFile() {
        String pathToWrrite = "Z:\\PROJECTS_JAVA\\037_RangersPL_AimMaster_Pary\\src\\pl\\brzozowski\\files\\pairs.txt";
        try {
            FileWriter fileWriter = new FileWriter(pathToWrrite);
            for (int i = 0; i < pairsOfPlayers.size(); i++) {
                fileWriter.write("Para " + (i+1) + " (" + pairsOfPlayers.get(i).getPlayer1().getNick() + ")\n");
                fileWriter.write(pairsOfPlayers.get(i).getPlayer1().getNick() + " z " + pairsOfPlayers.get(i).getPlayer2().getNick() + "\n\n");

            }
            fileWriter.close();
        } catch (IOException e) {
            logger.error("Blad zapisu do pliku {}",e);
        }
    }

    public ObservableList<Player> getAllPlayers() {
        return players;
    }

    public Player getPlayerSelected() {
        return playerSelected.get();
    }

    public ObservableObjectValue<Player> playerSelectedProperty() {
        return playerSelected;
    }

    public void setPlayerSelected(Player playerSelected) {
        this.playerSelected.set(playerSelected);
    }

    public Player getPlayerSelectedPresent() {
        return playerSelectedPresent.get();
    }

    public ObjectProperty<Player> playerSelectedPresentProperty() {
        return playerSelectedPresent;
    }

    public void setPlayerSelectedPresent(Player playerSelectedPresent) {
        this.playerSelectedPresent.set(playerSelectedPresent);
    }

    public ObservableList<Player> getPresentPlayers() {
        return presentPlayers;
    }

    public void setPresentPlayers(ObservableList<Player> presentPlayers) {
        this.presentPlayers = presentPlayers;
    }

    public List<Player> getAboveKDPlayers() {
        return aboveKDPlayers;
    }

    public List<Player> getBelowKDPlayers() {
        return belowKDPlayers;
    }

    public ObservableList<Pair> getPairsOfPlayers() {
        return pairsOfPlayers;
    }
}
