package giulio.frasca.silencesched;

import java.util.LinkedList;



public class Schedule {

	private LinkedList<RingerSettingBlock> blocks;
	private PrefReader reader;
	
	public Schedule(){
		blocks = new LinkedList<RingerSettingBlock>();
		loadBlocks();
	}
	
	public void loadBlocks(){
		reader = new PrefReader();
		RingerSettingBlock first = reader.getFirst();
		blocks.add(first);
		while (reader.hasNext()){
			blocks.add(reader.getNext());
		}
	}
	
	public void addBlock(String input){
		RingerSettingBlock newBlock = new RingerSettingBlock(getAlarmCount(),0);
		reader.addBlock(input);
		blocks.add(newBlock);
	}
	
	public void removeBlock(int id){
		
		
	}
	
	public void editBlock(int id, String fieldName, String val){
		
	}
	
	public int getAlarmCount(){
		return reader.getAlarmCount();
	}
}
