import java.util.*;
import java.util.function.*;

//класс почтового сервиса
public class MailService<T> implements Consumer<DopClass> {

    private Map<String, List<T>> NewMailBox;//словарь почтового ящика

    public MailService() {
        //анонимный класс или вложенный
        NewMailBox = new HashMap<String, List<T>>() {
            @Override
            public List<T> get(Object key) {
                List<T> list = super.get(key);//если ключ найден в мапе,то данные перезаписываются с новым значением
                if (list == null) list = new ArrayList<>();//возвращает значение если ключ есть и создаёт пустой linked list, если нет такого ключа
                return list;
            }
        };
    }

    public Map<String, List<T>> getMailBox() {
        return NewMailBox;
    }

    @Override

    public void accept(DopClass a) {
        if (NewMailBox == null) NewMailBox = new HashMap<>();
        //передаем имя пользователя
        if (NewMailBox.containsKey(a.getTo())) { //если для пользователя есть письма
            NewMailBox.get(a.getTo()).add((T) a.getContent());//извлекаем их в соответствие с ключом get(a.getTo()) и добавляем содержимое
        } else {
            NewMailBox.put(a.getTo(), new ArrayList<>());//если пользователя нет, то создаем новый ArrayList, отправляем
            NewMailBox.get(a.getTo()).add((T) a.getContent());//и извлекаем
        }
    }
}

