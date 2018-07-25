class purgeCommand {
    // An example of how to use Optional parameters
    @Command(label="!purge", usage = "!purge [Amount]", description = "Bulk-deletes messages.")
    fun handlePing(message: IMessage, @Optional amount: Long?) {
        if(amount == null) amount = 5;
        var history = message.channel.getMessageHistoryFrom(amount);
        history.bulkDelete();
    }
}
