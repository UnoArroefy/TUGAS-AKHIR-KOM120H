import java.util.*;

enum Status {
    WAITING, IN_PROGRESS, COMPLETED, CANCELLED
}

class QueueHistory {
    private Patient Patient;
    private int priority;
    private Status status;
    QueueHistory(Patient Patient, int priority, Status status) {
        this.Patient = Patient;
        this.priority = priority;
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public String toString() {
        return Patient.toString() + ", Priority: " + priority + ", Status: " + status;
    }
}

class History {
    private Node head;

    private static class Node {
        QueueHistory data;
        Node next;

        Node(QueueHistory data) {
            this.data = data;
            this.next = null;
        }
    }

    public void insert(QueueHistory data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public void delete(QueueHistory data) {
        Node current = head;
        Node previous = null;
        while (current != null && current.data.getPriority() != data.getPriority()) {
            previous = current;
            current = current.next;
        }
        if (current == null) {
            return;
        }
        if (previous == null) {
            head = current.next;
        } else {
            previous.next = current.next;
        }
    }

    public int size() {
        Node current = head;
        int size = 0;
        while (current != null) {
            size++;
            current = current.next;
        }
        return size;
    }

    public QueueHistory get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public String display() {
        StringBuilder result = new StringBuilder();
        Node current = head;
        while (current != null) {
            result.append(current.data.toString()).append("\n");
            current = current.next;
        }
        return result.toString();
    }

};

class HashMap {
    private static final int SIZE = 128;
    private Node[] table;

    private static class Node {
        int key;
        Patient value;
        Node next;

        Node(int key, Patient value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    HashMap() {
        table = new Node[SIZE];
    }

    private int hash(int key) {
        return key % SIZE;
    }

    public void put(int key, Patient value) {
        int index = hash(key);
        Node newNode = new Node(key, value);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node current = table[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public Patient get(int key) {
        int index = hash(key);
        Node current = table[index];
        while (current != null && current.key != key) {
            current = current.next;
        }
        if (current == null) {
            return null;
        }
        return current.value;
    }

    public void remove(int key) {
        int index = hash(key);
        Node current = table[index];
        Node previous = null;
        while (current != null && current.key != key) {
            previous = current;
            current = current.next;
        }
        if (current == null) {
            return;
        }
        if (previous == null) {
            table[index] = current.next;
        } else {
            previous.next = current.next;
        }
    }

    public boolean containsKey(int key) {
        int index = hash(key);
        Node current = table[index];
        while (current != null && current.key != key) {
            current = current.next;
        }
        return current != null;
    }

    public boolean isEmpty() {
        for (int i = 0; i < SIZE; i++) {
            if (table[i] != null) {
                return false;
            }
        }
        return true;
    }

    public int size() {
        int size = 0;
        for (int i = 0; i < SIZE; i++) {
            Node current = table[i];
            while (current != null) {
                size++;
                current = current.next;
            }
        }
        return size;
    }
}

class Antrian {
    private int maxSize;
    private int[] queueArray;
    private int front;
    private int rear;
    private int currentSize;

    public Antrian(int maxSize) {
        this.maxSize = maxSize;
        this.queueArray = new int[maxSize];
        this.front = 0;
        this.rear = -1;
        this.currentSize = 0;
    }

    public void enqueue(int item) {
        if (isFull()) {
            throw new Error("Queue is full. Cannot enqueue.");
        }
        rear = (rear + 1) % maxSize;
        queueArray[rear] = item;
        currentSize++;
    }

    public int dequeue() {
        if (isEmpty()) {
            throw new Error("Queue is empty. Cannot dequeue. bithc");
        }
        int dequeuedItem = queueArray[front];
        front = (front + 1) % maxSize;
        currentSize--;
        return dequeuedItem;
    }

    public int peek() {
        if (isEmpty()) {
            return -100;
        }
        return queueArray[front];
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public boolean isFull() {
        return currentSize == maxSize;
    }

    public int size() {
        return currentSize;
    }

    public void display() {
        if (isEmpty()) {
            throw new Error("Queue is empty. Cannot display.");
        }
        int i = front;
        while (i != rear) {
            System.out.print(queueArray[i] + " ");
            i = (i + 1) % maxSize;
        }
        System.out.println(queueArray[rear]);
    }
}

public class QueueManager { 
    private Antrian queue;
    private Antrian temp;
    private History history;
    private HashMap hashMap;
    private int tmpPriority = 0;

    QueueManager(int maxSize) {
        queue = new Antrian(maxSize);
        temp = new Antrian(maxSize);
        history = new History();
        hashMap = new HashMap();
    }

    public int enqueue(Patient patient, int priority) {
        queue.enqueue(priority);
        history.insert(new QueueHistory(patient, priority, Status.WAITING));
        hashMap.put(priority, patient);
        Random random = new Random();
        return (random.nextInt(5) + 15) * queue.size();
    }

    public void dequeue() {
        int priority = queue.dequeue();
        if (tmpPriority != priority && tmpPriority != 0) {
            Patient patient = hashMap.get(tmpPriority);
            history.insert(new QueueHistory(patient, tmpPriority, Status.COMPLETED));
        }
        Patient patient = hashMap.get(priority);
        history.insert(new QueueHistory(patient, priority, Status.IN_PROGRESS));
        tmpPriority = priority;
    }

    public void cancelqueue(int priority) {
        boolean isPresent = hashMap.containsKey(priority);
        if (!isPresent) {
            throw new Error("Not found");
        }
        while (!queue.isEmpty()) {
            if (queue.peek() == priority) {
                Patient patient = hashMap.get(priority);
                history.insert(new QueueHistory(patient, priority, Status.CANCELLED));
                queue.dequeue();
            } else {                
                temp.enqueue(queue.dequeue());
            }
        }
        while (!temp.isEmpty()){
            queue.enqueue(temp.dequeue());
        }
    }

    public int peek() {
        return queue.peek();
    }

    public String history() {
        return history.display();
    }

    public void history(int index) {
        System.out.println(history.get(index));
    }

    public void queue() {
        queue.display();
    }

    public int size() {
        return queue.size();
    }

}