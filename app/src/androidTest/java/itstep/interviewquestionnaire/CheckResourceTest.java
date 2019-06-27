package itstep.interviewquestionnaire;

import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

// Проверка ресурсов
@RunWith(AndroidJUnit4.class)
public class CheckResourceTest {

    private Resources resources = InstrumentationRegistry.getTargetContext().getResources();

    // Количество вопросов и количество правильных ответов
    @Test
    public void checkResource_QuestionsAndRightAnswers() throws Exception {
        int numberQuestions = resources.obtainTypedArray(R.array.questions).length();
        int numberRightAnswers = resources.getIntArray(R.array.rightAnswers).length;
        assertEquals("Number of questions and the number of correct answers", numberQuestions, numberRightAnswers);
    }

    // Количество очков за правильные ответы и количество правильных ответа
    @Test
    public void checkResource_PointsPerAnswersAndRightAnswers()throws Exception{
        int numberRightAnswers = resources.getIntArray(R.array.rightAnswers).length;
        int pointsPerAnswers = resources.getIntArray(R.array.pointsPerAnswers).length;
        assertEquals("The number of points for correct answers and the number of correct answers", pointsPerAnswers, numberRightAnswers);
    }

    // Количество очков за чекбоксы и количество чекбоксов
    @Test
    public void checkResource_PointsPerCheckboxAndNumberCheckBoxes() throws Exception {
        int numberCheckBoxes = resources.obtainTypedArray(R.array.checkBoxStrings).length();
        int pointsPerCheckbox = resources.getIntArray(R.array.pointsPerCheckbox).length;
        assertEquals("The number of points per checkboxes and the number of checkboxes", numberCheckBoxes, pointsPerCheckbox);
    }
}
