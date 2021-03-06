import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main {


    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);
        //list.forEach(System.out::println);

        String json = listToJson(list);
        //System.out.println(json);

        writeString(json, "new_data.json");

// Задача 2: XML - JSON парсер
        List<Employee> listXML = parseXML("data.xml");
        String jsonXML = listToJson(listXML);
        //System.out.println(jsonXML);

        writeString(jsonXML, "new_data_XML.json");

        //Задача 3: JSON парсер
        String jsonJSON = readString("new_data.json");
        System.out.println(jsonJSON);
        List<Employee> listJSON = jsonToList(jsonJSON);
        for (Employee employee : listJSON) {
            System.out.println(employee);
        }

    }


    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;
        try (CSVReader csvReader = new CSVReader(new FileReader("data.csv"))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping("id", "firstName", "lastName", "country", "age");
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();

            // list.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    private static String listToJson(List<Employee> list) {
        String json = null;
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        //System.out.println(gson.toJson(list));
        json = gson.toJson(list, listType);
        // System.out.println(gson.toJson(json));


        return json;
    }


    private static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> parseXML(String s) throws ParserConfigurationException, IOException, SAXException {

        long id = 0;
        String firstName = null;
        String lastName = null;
        String country = null;
        int age = 0;
        String textContent = null;
        String nodeName = null;
        Employee employee = new Employee();
        List<Employee> list = new ArrayList<Employee>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(s));
        Node root = doc.getDocumentElement();
        // System.out.println("Корневой элемент: " + root.getNodeName());

        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);

            if (Node.ELEMENT_NODE == node_.getNodeType()) {

                if (node_.getNodeType() == 1) {

                    nodeName = node_.getNodeName();
                    textContent = node_.getTextContent();
                    String peopleArray = textContent.replaceAll("\\s+", " ");
                    String[] ary = peopleArray.split(" ");
                    employee = new Employee(Long.parseLong(ary[1]), ary[2], ary[3], ary[4], Integer.parseInt(ary[5]));
                    list.add(employee);

                }

            }

        }
        return list;
    }

    private static String readString(String fileName) {
        String nextLine = null;
        StringBuilder str = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            while ((nextLine = br.readLine()) != null) {
                // System.out.println(nextLine);
                str.append(nextLine);
            }
            // System.out.println(str);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


        return str.toString();
    }

    private static List<Employee> jsonToList(String jsonJSON) {

        List<Employee> listJSON = new ArrayList<Employee>();

        Gson gson = new Gson();

        Type userListType = new TypeToken<List<Employee>>() {
        }.getType();

        listJSON = gson.fromJson(jsonJSON, userListType);


        return listJSON;
    }
}
