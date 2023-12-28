/** Starter code for P3
 *  @author
 */

// Change to your net id
//package sxm190157;
import java.util.*;
// If you want to create additional classes, place them in this file as subclasses of MDS

public class MDS {
    // Add fields of MDS here
    private Map<Integer, Item> productsById; //Hashmap that supports mapping from Id to item
    private Map<List<Integer>, TreeSet<Item>> descriptionIndex; //Hashmap that supports mapping description List to Item where Integer represents the hash value of the list of integers
    
    // Constructors
    public MDS() {
        productsById = new HashMap<>();
        descriptionIndex = new HashMap<>();
    }

    /* Public methods of MDS. Do not change their signatures.
       __________________________________________________________________
       a. Insert(id,price,list): insert a new item whose description is given
       in the list.  If an entry with the same id already exists, then its
       description and price are replaced by the new values, unless list
       is null or empty, in which case, just the price is updated. 
       Returns 1 if the item is new, and 0 otherwise.
    */
    
        /**
     * Inserts a new item into the MDS or updates the existing item.
     * @param id The ID of the item.
     * @param price The price of the item.
     * @param list The list representing the description of the item.
     * @return 1 if the item is new, 0 otherwise.
     */
     
    public int insert(int id, int price, java.util.List<Integer> list) {
	    if(list == null || list.isEmpty()){ //list is empty or null
	        if(productsById.containsKey(id)){ //if the id exists within an empty or null list 
	            Item item = productsById.get(id);
	            item.setPrice(price); //price is updated 
	            return 0; //returns 0 since item is old (not new)
	        }
	    }
	    else { //create new item, update the maps
	        Item newItem = new Item(id, price, list);
	        productsById.put(id, newItem);
	        descriptionIndex.computeIfAbsent(list, k -> new TreeSet<>()).add(newItem);
	        return 1;
	    }
	    return 0;
    }

    // b. Find(id): return price of item with given id (or 0, if not found).
        /**
     * Finds and returns the price of an item with the given ID.
     * @param id The ID of the item to find.
     * @return The price of the item, or 0 if the item is not found.
     */
     
    public int find(int id) {
	    if(productsById.containsKey(id)){ //if item with given id is found, return it's id with respective price
	        return productsById.get(id).getPrice(); 
	    }
	    return 0; //otherwise return 0 if not found
    }

    /* 
       c. Delete(id): delete item from storage.  Returns the sum of the
       ints that are in the description of the item deleted,
       or 0, if such an id did not exist.
    */
    
        /**
     * Deletes an item from storage and returns the sum of integers in its description.
     * @param id The ID of the item to delete.
     * @return The sum of integers in the description of the deleted item, or 0 if the item is not found.
     */
     
    public int delete(int id) {
	    if(productsById.containsKey(id)){ //if id exists...
	        Item item = productsById.get(id);
	        int sum = item.getDescriptionSum();
	        productsById.remove(id); //remove id
	        descriptionIndex.remove(item.getDescription()); //remove description
	        return sum; //return the sum of ints that are in the description of the item deleted
	    }
	    return 0; //otherwise return 0 if id doesn't exist
    }

    /* 
       d. FindMinPrice(n): given an integer, find items whose description
       contains that number (exact match with one of the ints in the
       item's description), and return lowest price of those items.
       Return 0 if there is no such item.
    */
    
       /**
     * Finds the lowest price among items whose description contains a specific number.
     * @param n The number to search for in item descriptions.
     * @return The lowest price among matching items, or 0 if there is no such item.
     */
     
    public int findMinPrice(int n) { // Loop through each TreeSet of Items in descriptionIndex
	    int minPrice = 0;
	    
	    for (TreeSet<Item> items: descriptionIndex.values()){ // Loop through each Item in the current TreeSet
	        for(Item item: items){
	            if(item.getDescription().contains(n)){ //keeps looping and updating the minPrice if there is a lower price value. Stops if minPrice value is reached
	                if(minPrice == 0 || item.getPrice() < minPrice){
	                    minPrice = item.getPrice();
	                }
	            }
	        }
	    }
	    return minPrice; //minPrice value is returned
    }

    /* 
       e. FindMaxPrice(n): given an integer, find items whose description
       contains that number, and return highest price of those items.
       Return 0 if there is no such item.
    */
    
        /**
     * Finds the highest price among items whose description contains a specific number.
     * @param n The number to search for in item descriptions.
     * @return The highest price among matching items, or 0 if there is no such item.
     */
     
    public int findMaxPrice(int n) { 
        int maxPrice = 0;
    
        for (TreeSet<Item> items : descriptionIndex.values()) {  // Loop through each TreeSet of Items in descriptionIndex
            for (Item item : items) { // Loop through each Item in the current TreeSet
                if (item.getDescription().contains(n)) { //keeps looping and updating the maxPrice if there is a higher price value. Stops if maxPrice value is reached
                    if (maxPrice == 0 || item.getPrice() > maxPrice) {
                        maxPrice = item.getPrice();
                    }
                }
            }
        }
        return maxPrice; //maxPrice is returned
    }


    /* 
       f. FindPriceRange(n,low,high): given int n, find the number
       of items whose description contains n, and in addition,
       their prices fall within the given range, [low, high].
    */
    
        /**
     * Finds the number of items whose description contains a specific number,
     * and their prices fall within a given range.
     * @param n The number to search for in item descriptions.
     * @param low The lower bound of the price range.
     * @param high The upper bound of the price range.
     * @return The count of matching items.
     */
    
    public int findPriceRange(int n, int low, int high) {
    	int count = 0;  // Initialize count as 0 to store count of items within the range
    	
    	for(TreeSet<Item> items: descriptionIndex.values()){// Loop through each TreeSet of Items in descriptionIndex
    	    for(Item item : items){// Loop through each Item in the current TreeSet
    	        List <Integer> description = item.getDescription(); //get item's description
    	        if(description.contains(n) && item.getPrice() >= low && item.getPrice() <= high){ // Check if the item's description contains 'n' and its price falls within the given range
    	            count++; //increment count if item price falls within the range and exists
    	        }
    	    }
    	}
    	return count; //return number of items with price that falls within the given range
    }

    /*
      g. RemoveNames(id, list): Remove elements of list from the description of id.
      It is possible that some of the items in the list are not in the
      id's description.  Return the sum of the numbers that are actually
      deleted from the description of id.  Return 0 if there is no such id.
    */
    
        /**
     * Removes elements from the description of an item and returns the sum of the removed numbers.
     * @param id The ID of the item.
     * @param list The list of elements to remove from the item's description.
     * @return The sum of numbers that are actually deleted from the description of id, or 0 if there is no such id.
     */
     
    public int removeNames(int id, java.util.List<Integer> list) {
        int sum = 0;
        
        if(productsById.containsKey(id)){ // Check if the ID exists in productsById map
            Item item = productsById.get(id); // Get the item associated with the ID
            List<Integer> description = item.getDescription(); // Get the item's description
            
            for(int name : list){
                if(description.contains(name)){ // Check if the item's description contains the current name
                    description.remove(Integer.valueOf(name)); // Remove the name from the description
                    sum+=name; // Add the removed name to the sum to deduce the total number of names removed
                }
            }
        }
        return sum; //total number of names removed
    }
    
        /**
     * Private inner class representing an item in the MDS.
     */
    
    private static class Item implements Comparable<Item> {
        private int id; //id of item
        private int price; //price of item
        private List<Integer> description; //item description
        
        public Item(int id, int price, List<Integer> description){
            this.id = id;
            this.price = price;
            this.description = new ArrayList<>(description);
        }
        
        public int getId(){
            return id;
        }
        
        public int getPrice(){
            return price;
        }
        
        public void setPrice(int price){
            this.price = price;
        }
        
        public List<Integer> getDescription(){
            return new ArrayList<>(description);
        }
        
        public void setDescription(List<Integer> description){
            this.description = new ArrayList<>(description);
        }
        
        public int getDescriptionSum(){
            return description.stream().mapToInt(Integer::intValue).sum();
        } 
        
        @Override
        public int compareTo(Item other){
            return Integer.compare(this.price, other.price); //compareTo method for comparing prices
        }
        
    }
}

