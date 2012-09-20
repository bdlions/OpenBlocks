package codegenerator;

class Person{
    private String name;
    private int age;
    private int passingYear;
    private float value;

	public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
	public int getAge(){
        return this.age;
    }
    public void setAge(int age){
        this.age = age;
    }
	public int getPassingYear(){
        return this.passingYear;
    }
    public void setPassingYear(int passingYear){
        this.passingYear = passingYear;
    }
	public float getValue(){
        return this.value;
    }
    public void setValue(float value){
        this.value = value;
    }
    public static void main(String args[]){
    }
}