package FinalProject;

public class Quiz {

    int number = 0;

    public String getQuestion() {
        return QuestionBank.questions[number];
    }

    public String[] getOptions() {
        return QuestionBank.options[number];
    }

    public int getAnswer() {
        return QuestionBank.answers[number];
    }

    public void nextQuestion() {
        number++;
    }

    public boolean hasNextQuestion() {
        return number < QuestionBank.questions.length - 1;
    }
    
    public int getTotal(){
        return QuestionBank.questions.length;
    }

    public void resetQuiz() {
        number = 0;
    }
}