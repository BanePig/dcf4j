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
        IDiscordClient client = createClient("NDU3OTI4MTQ3MTc0OTQ4ODY0.DjZnog.EqETDg0vj_ImnCtW2CNEIrsvZRE", true); // Build Discord4J client.
        if(client == null) return;
        while(!client.isReady() || !client.isLoggedIn()) { // Wait until client is logged in.
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        CommandDispatcher dispatcher = new CommandDispatcher(client); // Create command dispatcher.
        dispatcher.registerCommands(new Test()); // Register command.
    }

	// From discord4j's repository.
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

    @Command(name = "!ping", description = "Pongs your ping.", usage = "!ping")
    public void commandHandler(IMessage message) {
        messsage.getChannel().sendMessage("Pong!");
    }
	
	@Command(name = "!eat", description = "Eats a user.", usage = "!eat [User]")
    public void commandHandler(IMessage message, IUser user) {
        messsage.getChannel().sendMessage("Ate " + user.name() + "!");
    }
}
