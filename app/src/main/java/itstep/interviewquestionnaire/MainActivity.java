package itstep.interviewquestionnaire;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int NUMBER_WORDS_NAME = 3;     // Количество слов в поле Ф.И.О.
    private List<CheckBox> checkBoxes;                  // Чекбоксы
    private List<RadioGroup> answers;                   // Вопросы
    private EditText editText_name;                     // Поле ввода Ф.И.О.
    private EditText editText_age;                      // Поле ввода возраста
    private Button button_passTest;                     // Кнопка сдать тест

    // Валидация введенных данных для акивации кнопки "Сдать тест"
    TextWatcher textWatcherListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(button_passTest != null) {
                if (valuesIsValid()) {
                    button_passTest.setEnabled(true);
                } else {
                    button_passTest.setEnabled(false);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBoxes = new ArrayList<>();
        answers = new ArrayList<>();
        addViews();
        addListeners();
        initSeekBar();
    }

    // Добавление вопросов с ответами и чекбоксов
    private void addViews(){
        TextView textView;
        RadioGroup radioGroup;
        RadioButton radioButton;
        Resources resources = getResources();
        LayoutInflater layoutInflater = getLayoutInflater();

        // Получение вопросов с ответами
        TypedArray typedArray = resources.obtainTypedArray(R.array.questions);
        LinearLayout linearLayout = findViewById(R.id.id_container_questions);

        // Горизонтальная линия
        layoutInflater.inflate(R.layout.hr_line_template, linearLayout, true);

        // Количество вопросов
        int numberQuestions = typedArray.length();
        for (int i = 0; i < numberQuestions; i++){
            int id = typedArray.getResourceId(i, -1);
            if(id != -1){
                String [] question = resources.getStringArray(id);
                // Добавление вопроса
                textView =(TextView)layoutInflater.inflate(R.layout.text_radiogroup_template, null);
                textView.setText(question[0]);
                linearLayout.addView(textView);
                // Ответы
                radioGroup = new RadioGroup(this);
                for(int j = 1; j < question.length; j++){
                    // Создание кнопки с ответом
                    radioButton = (RadioButton)layoutInflater.inflate(R.layout.radiobutton_template, null);
                    radioButton.setText(question[j]);
                    radioButton.setTag(j);
                    radioGroup.addView(radioButton);
                }
                answers.add(radioGroup);
                linearLayout.addView(radioGroup);
                // Горизонтальная линия
                layoutInflater.inflate(R.layout.hr_line_template, linearLayout, true);
            }
        }
        typedArray.recycle();
        // Получение чекбоксов
        String [] checkBoxStrings = resources.getStringArray(R.array.checkBoxStrings);
        CheckBox checkBox;
        for(String string : checkBoxStrings){
            // Добавление чекбокса
            checkBox = (CheckBox)layoutInflater.inflate(R.layout.checkbox_template, null);
            checkBox.setText(string);
            checkBoxes.add(checkBox);
            linearLayout.addView(checkBox);
        }
    }
    // Добавление обработчиков
    private void addListeners(){
        editText_name = findViewById(R.id.id_editText_name);
        editText_name.addTextChangedListener(textWatcherListener);

        editText_age = findViewById(R.id.id_editText_age);
        editText_age.addTextChangedListener(textWatcherListener);

        button_passTest = findViewById(R.id.id_button_passTest);
    }

    // Иницилизация слайдера
    private void initSeekBar(){
        Resources resources = getResources();
        // Максимальная зарплата
        final int maximumWage = resources.getInteger(R.integer.maximumWageSeekBar);
        // Минимальная зарплата
        final int minimumWage = resources.getInteger(R.integer.minimumWageSeekBar);
        // Шаг выбора зарплаты
        final int stepWage = resources.getInteger(R.integer.stepWageSeekBar);
        // Поле с отображением зарплаты
        final TextView seekBarValue = findViewById(R.id.id_textView_seekBar);
        SeekBar seekBar = findViewById(R.id.id_seekBar_salary);
        seekBar.setMax((maximumWage - minimumWage) / stepWage);
        // Обработчик изменения значения
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(((progress * stepWage + minimumWage) + " $"));
                seekBarValue.setX(seekBar.getX() + seekBar.getThumb().getBounds().right + seekBar.getThumbOffset() - seekBarValue.getWidth());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarValue.setVisibility(View.VISIBLE);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarValue.setVisibility(View.INVISIBLE);
            }
        });
        seekBar.setProgress(seekBar.getMax() / 2);
    }
    // Валидация данных для активации кнопки
    private boolean valuesIsValid(){
        if(editText_name != null && editText_age != null){
            String string_name = editText_name.getText().toString();
            String string_age = editText_age.getText().toString();
            return !string_name.isEmpty() && string_name.split(" ").length == NUMBER_WORDS_NAME && !string_age.isEmpty() && string_age.matches("^\\d+$");
        }
        return false;
    }
    // Сдача теста
    public void buttonPassTestClick(View view) {
        Resources resources = getResources();
        // Проверка возраста
        if(isValidAge()){
            Toast.makeText(MainActivity.this, resources.getString(R.string.meetNotRequirementsAge), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, FailureActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        try {
            // Количество набранных болов
            int numberPoints = getNumberScores();
            // Необходимое количество балов
            int requiredNumberPoints = resources.getInteger(R.integer.requiredNumberPoints);
            if(numberPoints < requiredNumberPoints){
                Toast.makeText(MainActivity.this, resources.getString(R.string.rightAmountPoints), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, FailureActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        } catch (NoAllAnswersException e) {
            Toast.makeText(MainActivity.this, resources.getString(R.string.notAnswerAllQuestions), Toast.LENGTH_SHORT).show();
            return;
        }
        // Вызов окна
        Intent intent = new Intent(this, CommunicationActivity.class);
        startActivity(intent);
        finish();
    }
    // Проверка возраста
    private boolean isValidAge(){
        Resources resources = getResources();
        EditText editText = findViewById(R.id.id_editText_age);
        int age = Integer.parseInt(editText.getText().toString());
        int minAge = resources.getInteger(R.integer.minAge);
        int maxAge = resources.getInteger(R.integer.maxAge);
        return age < minAge || age > maxAge;
    }
    // Количество набранных балов
    private int getNumberScores() throws NoAllAnswersException{
        Resources resources = getResources();
        RadioButton radioButton;
        // Количество набранных болов
        int numberPoints = 0;
        // Правильные ответы
        int [] rightAnswers = resources.getIntArray(R.array.rightAnswers);
        // Балы за правильные ответы
        int [] pointsPerAnswers = resources.getIntArray(R.array.pointsPerAnswers);
        // Количество вопросв
        int numberAnswers = answers.size();
        for(int i = 0; i < numberAnswers; i++){
            int selectedId = answers.get(i).getCheckedRadioButtonId();
            if(selectedId == -1){
                throw new NoAllAnswersException();
            }
            radioButton = findViewById(selectedId);
            // Проверка на правильность ответа
            if(radioButton.getTag().equals(rightAnswers[i])){
                numberPoints += pointsPerAnswers[i];
            }
        }
        // Балы за чекбоксы
        int [] pointsPerCheckbox = resources.getIntArray(R.array.pointsPerCheckbox);
        // Количество чекбоксов
        int numberCheckBoxes = checkBoxes.size();
        for(int i = 0; i < numberCheckBoxes; i++){
            if (checkBoxes.get(i).isChecked()){
                numberPoints += pointsPerCheckbox[i];
            }
        }
        return numberPoints;
    }
}