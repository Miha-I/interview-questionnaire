package itstep.interviewquestionnaire;

// Исключение если нет ответов на все вопроы
public class NoAllAnswersException extends Exception {
    public NoAllAnswersException(){
        super();
    }
    public NoAllAnswersException(String message){
        super(message);
    }
}
