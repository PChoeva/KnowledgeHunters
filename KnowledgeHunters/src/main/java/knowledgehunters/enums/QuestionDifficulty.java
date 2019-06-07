package knowledgehunters.enums;

public enum QuestionDifficulty { 
    EASY("Лесно"), MEDIUM("Средно"), HARD("Трудно"); 
   
    private String value; 
    
    public String getValue() 
    { 
        return this.value; 
    } 
  
    // enum constructor - cannot be public or protected 
    private QuestionDifficulty(String value) 
    { 
        this.value = value; 
    } 
}


