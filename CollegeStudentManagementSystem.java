import java.util.*;

class Student {
    String id;
    String name;
    String course;
    double cgpa;

    Student(String id, String name, String course, double cgpa) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.cgpa = cgpa;
    }
}

public class CollegeStudentManagementSystem {
    static final int MAX = 100;
    static Student[] students = new Student[MAX];
    static int count = 0;
    static int nextIdNumber = 101;
    static Stack<String> undoStack = new Stack<>();

    static void addStudent(Scanner sc) {
        if (count >= MAX) {
            System.out.println("Student storage full.");
            return;
        }
        sc.nextLine();
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Course: ");
        String course = sc.nextLine();
        System.out.print("CGPA: ");
        double cgpa = sc.nextDouble();

        String id = "S" + nextIdNumber++;
        students[count++] = new Student(id, name, course, cgpa);
        undoStack.push("ADD:" + id);
        System.out.println("Student added. ID: " + id + " | " + name + " | " + course + " | " + cgpa);
    }

    static void displayAll() {
        if (count == 0) {
            System.out.println("No student records available.");
            return;
        }
        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + students[i].id + " | " + students[i].name + " | " + students[i].course + " | " + students[i].cgpa);
        }
    }

    static int binarySearchById(String targetId) {
        int low = 0;
        int high = count - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = students[mid].id.compareToIgnoreCase(targetId);
            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    static void sortById() {
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (students[j].id.compareToIgnoreCase(students[j + 1].id) > 0) {
                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                }
            }
        }
    }

    static void searchById(Scanner sc) {
        sc.nextLine();
        System.out.print("Student ID: ");
        String id = sc.nextLine().trim();

        sortById();
        int idx = binarySearchById(id);
        if (idx != -1) {
            Student s = students[idx];
            System.out.println("Found: " + s.id + " | " + s.name + " | " + s.course + " | " + s.cgpa);
        } else {
            System.out.println("Student ID " + id + " not found.");
        }
    }

    static void mergeSortByCGPA(Student[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortByCGPA(arr, left, mid);
            mergeSortByCGPA(arr, mid + 1, right);
            mergeByCGPA(arr, left, mid, right);
        }
    }

    static void mergeByCGPA(Student[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Student[] L = new Student[n1];
        Student[] R = new Student[n2];

        for (int i = 0; i < n1; i++) L[i] = arr[left + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i].cgpa >= R[j].cgpa) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    static void sortByCGPA() {
        if (count == 0) {
            System.out.println("No students to sort.");
            return;
        }
        mergeSortByCGPA(students, 0, count - 1);
        if (count == 1) {
            System.out.println("Sorted list (1 student):");
            System.out.println(students[0].id + " | " + students[0].name + " | " + students[0].course + " | " + students[0].cgpa);
        } else {
            for (int i = 0; i < count; i++) {
                System.out.println((i + 1) + ". " + students[i].id + " | " + students[i].name + " | " + students[i].cgpa);
            }
        }
    }

    static void deleteStudent(Scanner sc) {
        sc.nextLine();
        System.out.print("ID: ");
        String id = sc.nextLine().trim();

        int idx = -1;
        for (int i = 0; i < count; i++) {
            if (students[i].id.equalsIgnoreCase(id)) {
                idx = i;
                break;
            }
        }

        if (idx == -1) {
            System.out.println("Student ID " + id + " not found.");
            return;
        }

        String removedId = students[idx].id;
        for (int i = idx; i < count - 1; i++) {
            students[i] = students[i + 1];
        }
        students[--count] = null;
        undoStack.push("DELETE:" + removedId);
        System.out.println("Student " + removedId + " deleted.");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== COLLEGE STUDENT MANAGEMENT SYSTEM ===");
            System.out.println("1. Add student");
            System.out.println("2. View all students");
            System.out.println("3. Search by ID");
            System.out.println("4. Sort by CGPA");
            System.out.println("5. Delete student");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addStudent(sc);
                    break;
                case 2:
                    displayAll();
                    break;
                case 3:
                    searchById(sc);
                    break;
                case 4:
                    sortByCGPA();
                    break;
                case 5:
                    deleteStudent(sc);
                    break;
                case 6:
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}