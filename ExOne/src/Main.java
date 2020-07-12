
//
//главный класс
//
public class Main {
    //главный метод, содержащий тест
    public static void main(String[] args) {
        // набор ключевых слов для анализа на спам
        String[] spamKeywords = {"spam", "bad", "boring", "bullshit"};
        //максимальная длина комментария
        int commentMaxLength = 40;
        //набор анализаторов
        TextAnalyzer[] commentAnalyzer = {
                new SpamAnalyzer(spamKeywords),
                new NegativeTextAnalyzer(),
                new PositiveTextAnalyzer(),
                new TooLongTextAnalyzer(commentMaxLength)
        };

        // тестовые комментарии
        String[] tests = new String[8];
        tests[0] = "This comment is so good.";
        tests[1] = "This comment is so Loooooooooooooooooooooooooooong.";
        tests[2] = "Very negative comment=(((((((( !!!!=!!!!;";
        tests[3] = "Very positive comment  :)))); ";
        tests[4] = "This comment is so bad....";
        tests[5] = "The comment is a spam, maybeeeeeeeeeeeeeeeeeeeeee!";
        tests[6] = "Negative bad :( spam and positive))).";
        tests[7] = "Very bad, very neg =(, very ...bullshit!!!!!!!!!!!!!!!!!!!!";
        //создаем экзэмпляр данного класса для вызова метода из него
        Main testObject = new Main();
        int numberOfTest = 1; // номер тестового комментария
        result analisAll = new result(tests.length); //в этом экземпляре будут подсчитаны результаты
        //цикл по тестовым комментариям
        for (String test : tests) {
            System.out.println("test №" + numberOfTest + ": "+test);
            analisAll.add( testObject.checkLabels(commentAnalyzer, test)); //комментарий и массив анализаторов
            // отправляем в checkLabels, который анализирует комент, составляет для него экзэмпляр result, а затем
            //add() складывает полученный экзэмпляр с общим analisAll
            numberOfTest++;
        }
        analisAll.print(); //вывод результата
    }
    //метод, прогоняющий комент по анализаторам и составляющий для него result
    public result checkLabels(TextAnalyzer[] analyzers, String text) {
        result analis = new result();
        int numberOfAnalyzer=1; //номер анализатора
        Label res;
        for (TextAnalyzer analyzer : analyzers) { //цикл по анализаторам
            res = analyzer.processText(text);
            analis.signsCom(res);
            System.out.print(numberOfAnalyzer + ": ");//вывод текущего результата анализа
            System.out.println(res);
            numberOfAnalyzer++;

        }
        return analis;
    }
}
//
//класс для итогового подведения итогов
//
class result {
    //индексы по каждому фильтру для подсчета соответствующих комментариев
    private int neg;
    private int pos;
    private int lon;
    private int spam;
    private int norm;
    private double count;
    //конструктор без параметра, в случае анализа одного комментария
    public result()
    {count = 1;
        neg=0;
        pos=0;
        lon=0;
        spam=0;
        norm=0;
    }
    //конструктор с параметром
    public result(int Count)
    {   this();
        count = Count;
    }
    //итоговый подсчет и вывод
    public void print()
    {
        System.out.println("lon-"+lon+" neg-"+neg+" pos-"+pos+" spam-"+spam+" norm-"+norm);
        System.out.println("Процент от общего числа:\nСлишком длинных комментариев: "+new java.text.DecimalFormat("#0.00").format(lon*count/100) + "%");
        System.out.println("Негативных комментариев: "+new java.text.DecimalFormat("#0.00").format(neg*count/100) + "%");
        System.out.println("Позитивных комментариев: "+new java.text.DecimalFormat("#0.00").format(pos*count/100) + "%");
        System.out.println("Комментариев, содержащих спам: "+new java.text.DecimalFormat("#0.00").format(spam*count/100) + "%");
        System.out.println("Остальных комментариев: "+new java.text.DecimalFormat("#0.00").format(norm*count/100) + "%");

    }
    //заполнение показателей при анализе комментария
    public void signsCom(Label signs)
    {
        if(signs == Label.TOO_LONG)
            lon++;
        else if(signs == Label.NEGATIVE_TEXT)
            neg++;
        else if(signs == Label.POSITIVE_TEXT)
            pos++;
        else if(signs == Label.SPAM)
            spam++;
    }
    //сложение двух объектов
    public void add(result other)
    {
        if(other.lon+other.neg+other.pos+other.spam == 0)
            norm++;
        else {
            if (other.neg != other.pos)
            { neg+=other.neg; pos+=other.pos;}
            lon+=other.lon;spam+=other.spam;
        }
    }
}
//
//абстрактный класс из условия для реализации методов анализа спама, негативного и позитивного текста
//
abstract class KeywordAnalyzer implements TextAnalyzer { //реализация интерфейса
    protected abstract String[] getKeywords(); //когда от реализации можно отказаться используется abstract
    protected abstract Label getLabel();

    public Label processText(String text) { //общий метод, проверяющий комментарию на налицие строки из списка строк
        String[] keywords = getKeywords();
        for (String keyword : keywords)
            if (text.contains(keyword))
                return getLabel();
        return Label.NO;
    }
}
//
//интерфейс из условия
//
interface TextAnalyzer {
    Label processText(String text);
}

//
//набор признаков,которыми может обладать комментарий
//
enum Label {
    SPAM, NEGATIVE_TEXT, TOO_LONG, NO, POSITIVE_TEXT
}

//
//класс для проверки на спам
//
class SpamAnalyzer extends KeywordAnalyzer  {//SpamAnalyzer наследуется от KeywordAnalyzer
    private String[] keywords;
    //SpamAnalyzer проверяет строки и сравнивает с сохраненными в масиве
    //если есть совпадение, выводит как СПАМ
    public SpamAnalyzer(String[] keywords) {
        this.keywords = keywords;
    }

    @Override
    protected String[] getKeywords() {
        return keywords;
    }

    @Override
    protected Label getLabel() {
        return Label.SPAM;
    }
}
//NegativeTextAnalyzer сравнивает с массивом недовольных смайлов
//если есть совпадение - выводит НЕГАТИВ_ТЕКСТ
class NegativeTextAnalyzer extends KeywordAnalyzer  {
    private final String[] KEYWORDS = {":(", "=(", ":|"};

    @Override
    protected String[] getKeywords() {
        return KEYWORDS;
    }

    @Override
    protected Label getLabel() {
        return Label.NEGATIVE_TEXT;
    }

}

//
//класс для проверки на пози наличие позитивного текста

//
class PositiveTextAnalyzer extends KeywordAnalyzer  {
    private final String[] KEYWORDS = {":)", "=)", ";)", ":-)", "^_^", ")))"};

    @Override
    protected String[] getKeywords() {
        return KEYWORDS;
    }

    @Override
    protected Label getLabel() {
        return Label.POSITIVE_TEXT;
    }

}
//проверка на длину текста. Задается максимальный параметр длины maxLength
//далее возвращается длина введеных значений, сравнивается с maxLength
class TooLongTextAnalyzer implements TextAnalyzer  {
    private int maxLength;

    public TooLongTextAnalyzer(int threshold) {
        this.maxLength = threshold;
    }

    @Override
    public Label processText(String text) {
        if (text.length() > maxLength)
            return Label.TOO_LONG;
        else
            return Label.NO;
    }

}


