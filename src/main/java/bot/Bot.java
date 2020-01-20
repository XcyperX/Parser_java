package main.java.bot;

import main.java.SqlSand;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;

public class Bot extends TelegramLongPollingBot {
    private SqlSand sqlSand = new SqlSand();
    public static void main(String[] args) {
        ApiContextInitializer.init(); // Инициализируем апи
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        return "Footbol_result";
        //возвращаем юзера
    }

    @Override
    public void onUpdateReceived(Update e) {
        ArrayList<String> result;
        Message msg = e.getMessage(); // Это нам понадобится
        String txt = msg.getText();
        String[] info = txt.split(" ");
        if (txt.equals("/start")) {
            sendMsg(msg, "Hello, world! This is simple bot!");
        } else if (info[0].equals("Матчи") && info[2].equals("время")) {
            try {
                result = sqlSand.selectDataAndTime(info[1], info[3]);
                for (String resultInfo : result) {
                    sendMsg(msg, resultInfo);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // Тут будет то, что выполняется при получении сообщения
    }

    @Override
    public String getBotToken() {
        return "961357837:AAFIzY0yeqaw_0zz-KBu1z7aAIXnCVbf8ws";
        //Токен бота
    }

    @SuppressWarnings("deprecation")
    private void sendMsg(Message msg, String text) {
        SendMessage message = new SendMessage();
        message.setChatId((long) 716536607);
        message.setText(text);
        try { //Чтобы не крашнулась программа при вылете Exception
            message.setText(text);
            execute(message);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void sendMatchResult(String text) {
        SendMessage message = new SendMessage();
        message.setChatId((long) 716536607);
        message.setText(text);
        try { //Чтобы не крашнулась программа при вылете Exception
            message.setText(text);
            execute(message);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
