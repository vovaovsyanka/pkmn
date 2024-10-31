package ru.mirea.pkmn;
import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.ovsyannikovvi.web.http.PkmnHttpClient;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class CardImport {
    private String[] reportData;

    public CardImport() {}

    private void Import(String filename) throws IOException {
        FileInputStream fileinput = new FileInputStream(filename);
        BufferedInputStream bufferedinput = new BufferedInputStream(fileinput);
        byte[] data = bufferedinput.readAllBytes();
        reportData = new String(data).split("\r\n");
        fileinput.close();
        bufferedinput.close();
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
    public Card Fill(String filename) throws IOException {
        Import(filename);
        Card card_from = null;
        if (!reportData[4].equals("-")) {
            card_from = new Card();
            CardImport cardimport = new CardImport();
            card_from = cardimport.Fill(reportData[4]);
        }

        List<AttackSkill> skills = new ArrayList<>();
        String[] first = reportData[5].split(",");
        String[][] second = new String[first.length][];
        for (int i = 0; i < first.length; i++) {
            second[i] = first[i].split("/");
            AttackSkill attackSkill = new AttackSkill(second[i][1], "-", second[i][0], Integer.parseInt(second[i][2]));
            skills.add(attackSkill);
        }

        String[] student_ = reportData[11].split("/");
        Student student = new Student(student_[1], student_[0], student_[2], student_[3]);

        EnergyType weakness;
        try {
            weakness = EnergyType.valueOf(reportData[6].toUpperCase());
        } catch (IllegalArgumentException e) {
            weakness=null;
        }

        EnergyType resistance;
        try {
            resistance = EnergyType.valueOf(reportData[7].toUpperCase());
        } catch (IllegalArgumentException e) {
            resistance = null;
        }

        Card card = new Card(PokemonStage.valueOf(reportData[0].toUpperCase()), reportData[1], Integer.parseInt(reportData[2]),
                EnergyType.valueOf(reportData[3].toUpperCase()), card_from, skills, weakness,
                resistance, reportData[8], reportData[9], reportData[10].charAt(0), student, reportData[12]);

        PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();
        JsonNode card1 = pkmnHttpClient.getPokemonCard(reportData[1], reportData[12]);

        List<String> texts = new ArrayList<>();
        card1.findValues("attacks")
                .forEach(attackNode -> attackNode.forEach(attack -> {
                    String text = attack.get("text").asText();
                    texts.add(text);
                }));

        List<AttackSkill> skills1 = card.getSkills();
        int i=0;
        for (AttackSkill skill : skills1) {
            skill.setDescription(texts.get(i));
            i++;
        }
        return card;
    }
}
