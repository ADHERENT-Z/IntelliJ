package zyw.iterator;


public class NameIterator implements Iterator {

    public String[] names = {"Robert" , "John" ,"Julie" , "Lora"};

    int index;

    @Override
    public boolean hasNext() {
        if(index < names.length){
            return true;
        }
        return false;
    }

    @Override
    public Object next() {
        if(this.hasNext()){
            return names[index++];
        }
        return null;
    }
}

