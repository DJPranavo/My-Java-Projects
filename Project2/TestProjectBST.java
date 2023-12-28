import java.io.*;
import java.util.ArrayList;

class TestProjectBST
{
    BufferedReader inputFileReader;
    KeywordsArray keys;
	public static void main(String[] args) {
		TestProjectBST test = new TestProjectBST("datafile.txt");
		
	}
	
	public TestProjectBST(String filename){
	    try{ // in try/catch block to catch FileNotFoundException
	         // from java source code of FileReader(String)
	         // Java definition: public FileReader(String filename) throws FileNotFoundException
    	    inputFileReader = new BufferedReader(new FileReader(filename));
    	    keys = new KeywordsArray();
    	    if(inputFileReader == null){
    	        System.out.println("Error: you must open the file first!");
    	    }
    	    else{
    	        while(readNextRecord());
    	    }
            keys.print();
	    }
	    catch(IOException e){
	        e.printStackTrace();
	    }
	}
	public boolean readNextRecord(){
	    try{
    	    String data = inputFileReader.readLine(); //read first line of the file into a string
    	    if(data == null) return false;
    	    //System.out.printf("Test %s\n", data); -> prints out first line of file
    	    int titleId = Integer.parseInt(data);
    	    String title = inputFileReader.readLine();
    	    String author = inputFileReader.readLine();
    	    int numberOfKeys = Integer.parseInt(inputFileReader.readLine());
    	    //System.out.printf("Test: \n%d\n%s\n%s\n%d\n", titleId, title, author, numberOfKeys);
    	    
    	    Article a = new Article(titleId, title, author);
    	    //System.out.printf("%s\n",a);

    	    
    	    String keyword;
    	    for(int i = 0; i < numberOfKeys; i++){
    	        keyword = inputFileReader.readLine();
    	        keys.addToKeywordsArray(keyword);
    	        //System.out.printf("%s\n", keyword);
    	    }
    	    inputFileReader.readLine();
  
	    }
	    catch(IOException e){
	        System.out.printf("%s\n",e);
	        return false;
	    }
	    
	    return true;
	}
}

class Article{
    int id;
    String title;
    String author;
    
    public Article(int i, String t, String a){
        id = i;
        title = t;
        author = a;
    }
    
    @Override
    public String toString(){
        return String.format("\t %d | %s | %s->\n", id, title, author);
    }
    
}

class KeywordsArray{
    
    protected ArrayList<String>keys;
    public KeywordsArray(){keys = new ArrayList<String>();}
    
    public boolean addToKeywordsArray(String s){
        keys.add(s);
        return true;
    }
    
    public void print(){
        //remove duplicate words
        for(int i=0; i<keys.size(); i++) {
			for(int j=i+1; j<keys.size(); j++) {
				if(keys.get(i).equals(keys.get(j))) {
					keys.remove(j);
				}
			}
		}
		
		for (String element : keys){
		    element.trim();
            System.out.printf("%s\n", element);
        }
		
    }
}

class Element {
    String keyword;
    MyLinkedList<Article> head;
    MyLinkedList<Article> tail;
    
    //All the getters
    public String getKeyword(){
        return keyword;
    }
    
    //All the setters
    public void setKeyword (String keyword){
        this.keyword = keyword;
    }
    
}

/*
currently it is adding Article to the keyWords array
- change so it adds unique keywords (should not repeat) -> done
- some keywords have trailing spaces; use trim function on Strings -> done
- create a new object with 2 attributes (String and head)
- head is of type Article

*/