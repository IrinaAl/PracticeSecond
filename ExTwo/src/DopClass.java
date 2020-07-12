



//абстрактный generic класс, подходящий как для работы с письмами (String), так и с зарплатами(Integer)
public abstract class DopClass<T> {

    private String sender; //отправитель
    private String recipient;//получатель
    private T contentsOfLetter;//письмо
    //конструктор
    public DopClass(String sender, String recipient, T content) {
        this.sender = sender;
        this.recipient = recipient;
        this.contentsOfLetter = content;
    }

    public String getFrom() {
        return sender;
    }

    public String getTo() {
        return recipient;
    }

    public T getContent() {
        return contentsOfLetter;
    }
}

