package ru.mirea.ovsyannikovvi.pkmn;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class CardImport {
    private String[] reportData;

    public CardImport() {}

    private void Import(String filenameI) throws IOException {
        FileInputStream fileinput = new FileInputStream(filenameI);
        BufferedInputStream bufferedinput = new BufferedInputStream(fileinput);
        byte[] data = bufferedinput.readAllBytes();
        reportData = new String(data, "UTF-8").split("\n");
        fileinput.close();
        bufferedinput.close();
    }

    public PokemonStage ToPokemonStage(String input){
        if (input=="BASIC"){
            return PokemonStage.BASIC;
        } else if (input=="STAGE1") {
            return PokemonStage.STAGE1;
        } else if (input=="STAGE2") {
            return PokemonStage.STAGE2;
        } else if (input=="VSTAR") {
            return PokemonStage.VSTAR;
        } else if (input=="VMAX") {
            return PokemonStage.VMAX;
        }
        return null;
    }

    public EnergyType ToEnergyType(String input){
        if (input=="FIR"){
            return EnergyType.FIRE;
        } else if (input=="G") {
            return EnergyType.GRASS;
        } else if (input=="W") {
            return EnergyType.WATER;
        } else if (input=="L") {
            return EnergyType.LIGHTNING;
        } else if (input=="P") {
            return EnergyType.PSYCHIC;
        } else if (input=="FIG") {
            return EnergyType.FIGHTING;
        } else if (input=="DA") {
            return EnergyType.DARKNESS;
        } else if (input=="M") {
            return EnergyType.METAL;
        } else if (input=="FA") {
            return EnergyType.FAIRY;
        } else if (input=="DR") {
            return EnergyType.DRAGON;
        } else if (input=="C") {
            return EnergyType.COLORLESS;
        }
        return null;
    }
    public Card Deserialize(String fileName) {
        Card card = null;
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            card = (Card) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при импорте карточки: " + e.getMessage());
        }
        return card;
    }
    public Card Fill(String filenameF) throws IOException {
        Import(filenameF);

        reportData[0]="BASIC";
        reportData[1]="Croagunk";
        reportData[2]="70";
        reportData[3]="FIG";
        reportData[4]="-";
        reportData[5]="10/Poison Sting/FIG";
        reportData[6]="P";
        reportData[7]="-";
        reportData[8]="1";
        reportData[9]="Scarlet & Violet—Paldea Evolved";
        reportData[10]="G";
        reportData[11]="Ovsyannikov/Vladimir/Ivanovich/BSBO-04-23";

        Card card_from = null;
        if (reportData[4] != ("-")) {
            card_from = new Card();
            CardImport cardimport = new CardImport();
            card_from = cardimport.Fill(reportData[4]);
        }

        List<AttackSkill> skills = new ArrayList<>();
        String[] first = reportData[5].split(",");
        String[][] second = new String[first.length][];
        for (int i = 0; i < first.length; i++) {
            second[i] = first[i].split("/");
        }
        for (int i = 0; i < first.length; i=i+3) {
            AttackSkill attackSkill = new AttackSkill(second[i][1], "-", second[i][2], Integer.parseInt(second[i][0]));
            skills.add(attackSkill);
        }

        String[] student_ = reportData[11].split("/");
        Student student = new Student(student_[1], student_[0], student_[2], student_[3]);

        Card card = new Card(ToPokemonStage(reportData[0]), reportData[1], Integer.parseInt(reportData[2]),
                ToEnergyType(reportData[3]), card_from, skills, ToEnergyType(reportData[6]),
                ToEnergyType(reportData[7]), reportData[8], reportData[9], reportData[10].charAt(0),
                student);

        return card;
    }
}
