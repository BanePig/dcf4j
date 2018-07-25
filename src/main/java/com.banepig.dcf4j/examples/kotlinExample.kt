import com.banepig.dcf4j.Command
import com.banepig.dcf4j.Optional
import sx.blah.discord.handle.obj.IMessage

class purgeCommand {
    // An example of how to use Optional parameters
    @Command(label="!purge", usage = "!purge [Amount]", description = "Bulk-deletes messages.")
    fun handlePing(message: IMessage, @Optional amount: Long?) {
        var finalAmount = amount
        if (amount == null) finalAmount = 5
        var history = message.channel.getMessageHistoryFrom(finalAmount!!)
        history.bulkDelete()
    }
}
