package mebiuw.rbb.fundation.protocol.chord;

public class Test {

	public static void main(String[] args) {
		//this.source+"#"+this.id+"#"+this.hops+"#"+this.processTime+"#"+this.createdTime+"#"+this.endTime+"#"+this.type+"#"+this.entry
		ChordMessage msg=new ChordMessage("64","127.0.0.1","INSERT","1",1);
		System.out.println(msg.getMessageText());
		ChordMessage nsg=new ChordMessage(msg.getMessageText(),"127.0.0.1","INSERT","1",1);
		System.out.println(nsg.getMessageEntry());
		nsg=new ChordMessage(nsg.getMessageEntry());
		System.out.println(nsg.getMessageText());
	}

}
