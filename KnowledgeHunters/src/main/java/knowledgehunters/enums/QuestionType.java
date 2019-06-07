package knowledgehunters.enums;

public enum QuestionType {
	 
	MULTIPLE_CHOICE("А,Б,В,Г"), TRUE_FALSE("Вярно/Грешно"), OPEN("Отворен"), MATCH("Свързване"),  SORT("Подреждане");
	   
	private String value; 
    
	// enum constructor - cannot be public or protected 
    private QuestionType(String value)
    { 
    	this.value = value; 
    } 
	
    public String getValue() 
    { 
        return this.value; 
    } 
}
