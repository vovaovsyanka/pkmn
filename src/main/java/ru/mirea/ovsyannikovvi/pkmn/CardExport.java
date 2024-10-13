package ru.mirea.ovsyannikovvi.pkmn;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CardExport{
    private Card card;

    public CardExport() {}

    public CardExport(Card card) {
        this.card = card;
    }
    public void Serialize() {
        String fileName = "C:\\Users\\Ovsyanka\\IdeaProjects\\pkmn\\src\\main\\resources\\"+card.getName() + ".crd";
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(card);
        } catch (IOException e) {
            System.err.println("Ошибка при экспорте карточки: " + e.getMessage());
        }
    }

}
