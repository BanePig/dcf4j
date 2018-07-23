package com.banepig.dcf4j.test;

import com.banepig.dcf4j.Command;
import com.banepig.dcf4j.CommandDispatcher;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;

public class Test {
    public static void main(String... args) {
        IDiscordClient client = createClient("NDU3OTI4MTQ3MTc0OTQ4ODY0.DjZnog.EqETDg0vj_ImnCtW2CNEIrsvZRE", true);
        if(client == null) return;
        while(!client.isReady() || !client.isLoggedIn()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        CommandDispatcher dispatcher = new CommandDispatcher(client);
        dispatcher.registerCommands(new Test());
    }

    private static IDiscordClient createClient(String token, boolean login) { // Returns a new instance of the Discord client
        ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
        clientBuilder.withToken(token); // Adds the login info to the builder
        try {
            if (login) {
                return clientBuilder.login(); // Creates the client instance and logs the client in
            } else {
                return clientBuilder.build(); // Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
            }
        } catch (DiscordException e) { // This is thrown if there was a problem building the client
            e.printStackTrace();
            return null;
        }
    }

    @Command(name = "!sick", description = "Eats man", usage = "!sick [Name] [User]")
    public void commandHandler(IMessage message, String name, IUser poopoo) {
        System.out.println(message);
        System.out.println(name);
        System.out.println(poopoo);
        message.getChannel().sendMessage("Coolcool");
    }
}
