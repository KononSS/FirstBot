package com.example.FirstBot.service;
import com.example.FirstBot.config.BotConfig;
import com.example.FirstBot.model.Pogoda;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SpringBot extends TelegramLongPollingBot {

    final BotConfig config;

    public SpringBot(BotConfig config) {
        this.config = config;
        List< BotCommand> listofCommand=new ArrayList<>();
        listofCommand.add(new BotCommand("/start", "Привет! Жмякай на \"старт\", чтобы перезапустить бота"));
        listofCommand.add(new BotCommand("/squonch", "Жмякай сюда, если устал"));
        try {
            this.execute(new SetMyCommands(listofCommand,new BotCommandScopeDefault(),null));
        }
        catch (TelegramApiException e){
            log.error("Error setting bot's command list: "+e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText=update.getMessage().getText();
            long chatId=update.getMessage().getChatId();

            switch (messageText){
                case "/start":
                    startCommandReceived(chatId,update.getMessage().getChat().getFirstName());
                    break;
                case "/squonch":
                    squonchCommandReceived(chatId,update.getMessage().getChat().getFirstName());
                    break;
                default:
                    try {
                        Pogoda data=getUrlContent("http://api.openweathermap.org/data/2.5/weather?q="
                                +messageText +"&appid=86012da3daf63a7b17e714ed579f2963&units=metric");
                        weatherOutput(chatId, data, messageText);
                    } catch (Exception e) {
                        sendMessage(chatId,"Извини браток, точно не ошибся?");
                    }
            }
        }
        System.out.println(update.getMessage().getChat().getFirstName()+"   "
                + update.getMessage().getChat().getLastName()+"   "+update.getMessage().getChat().getUserName()
                +"\n"+update.getMessage().getText());
    }
    @Override
    public String getBotUsername() {return config.getBotName();}
    public String getBotToken(){return config.getToken();}
    private static Pogoda getUrlContent(String urlAdress) throws Exception {
        URL url = new URL(urlAdress);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            builder.append(line);
        reader.close();
        String html = builder.toString();
        Gson gson = new Gson();
        Pogoda myObject = gson.fromJson(html, Pogoda.class);
        System.out.println(html);
        System.out.println(myObject.toString());
        return myObject;
    }
    private void weatherOutput(long chatId, Pogoda p, String city){
        String answer = "В городе "+city+" следующие погодные условия: \n"+p.getMain()+p.getWind();
        sendMessage(chatId, answer);
        log.info("Replied to user");
    }
    private void startCommandReceived(long chatId, String name){
        String answer = "Привет,"+name+"! Рад видеть тебя в этом чате! Нажмякай свой город и я покажу тебе погоду!))) ";
        sendMessage(chatId, answer);
        log.info("Replied to user "+name);
    }
    private void squonchCommandReceived(long chatId, String name){
        String r = "https://pornhub.com";
        String answer = name+", загляни сюда ➡"+r;
        sendMessage(chatId, answer);
        log.info("Replied to user "+name);
    }
    private void sendMessage(long chatId, String textToSend){
        SendMessage message=new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        }
        catch (TelegramApiException e){
            log.error("Error occurred: "+e.getMessage());
        }
    }
}
