package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

import model.Campo;

public class Manager {
    private static Manager manager;
    private ArrayList<Document> entradas;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private Document b;
    private Document c;
    private Map<Document, List<Document>> bodegaVidsMap;
    private List<Document> camposRecolectados;
    private List<Campo> campos;
    
    private Manager() {
        this.entradas = new ArrayList<>();
        this.bodegaVidsMap = new HashMap<>();
        this.camposRecolectados = new ArrayList<>();
    }
    
    public static Manager getInstance() {
        if (manager == null) {
            manager = new Manager();
        }
        return manager;
    }
	
    private void createSession() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("viticultura");
    }

	public void init() {
		createSession();
		getEntrada();
		manageActions();
		showAllCampos();
		mongoClient.close();
	}

	private void manageActions() {
        for (Document entrada : this.entradas) {
            String instruccion = entrada.getString("instruccion").replace("'", "").trim();
            String[] split = instruccion.toUpperCase().split(" ");

            switch (split[0]) {
                case "B":
                    addBodega(split);
                    break;
                case "C":
                    addCampo(split, false);
                    break;
                case "V":
                    addVid(split);
                    break;
                case "M":
                    vendimiado(split);
                    break;
                case "#":
                    vendimia();
                    break;
                default:
                    System.out.println("Instruccion incorrecta");
            }
        }
    }

	private void vendimia() {
        for (Map.Entry<Document, List<Document>> entry : bodegaVidsMap.entrySet()) {
            Document bodega = entry.getKey();
            List<Document> vids = entry.getValue();
            bodega.put("vids", vids);
        }
        
        for (Document campo : camposRecolectados) {
            if (campo.containsKey("vids")) {
                campo.put("recolectado", true);
                collection = database.getCollection("Campo");
                Document updateQuery = new Document();
                updateQuery.append("$set", new Document().append("recolectado", true));
                collection.updateOne(new Document("_id", campo.get("_id")), updateQuery);
            }
        }
        
        bodegaVidsMap.clear();
    }

	private void addVid(String[] split) {
        Document v = new Document();
        v.put("tipo", split[1].toUpperCase());
        v.put("cantidad", Integer.parseInt(split[2]));
        v.put("bodega", b.get("_id")); 
        v.put("campo", c.get("_id"));
        collection = database.getCollection("Vid");
        collection.insertOne(v);
        List<Document> vids = (List<Document>) c.get("vids");
        if (vids == null) {
            vids = new ArrayList<>();
        }
        vids.add(v);
        c.put("vids", vids);
        Document updateQuery = new Document();
        updateQuery.append("$set", new Document().append("vids", vids));
        collection.updateOne(new Document("_id", c.get("_id")), updateQuery);
        List<Document> bodegaVids = bodegaVidsMap.getOrDefault(b, new ArrayList<>());
        bodegaVids.add(v);
        bodegaVidsMap.put(b, bodegaVids);
    }

	private void addCampo(String[] split, boolean vendimiado) {
        try {
            Document document = new Document();
            document.put("vendimiado", vendimiado);
            collection = database.getCollection("campo");
            collection.insertOne(document);
            System.out.println("Campo agregado correctamente.");
            String nuevoCampoId = document.getObjectId("_id").toString();
            Campo nuevoCampo = new Campo();
            nuevoCampo.setId(nuevoCampoId);
            nuevoCampo.setVendimiado(vendimiado);
            campos.add(nuevoCampo);
        } catch (Exception e) {
            System.out.println("Error al agregar el campo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String getLastCampoId() {
        MongoCollection<Document> campoCollection = database.getCollection("campo");
        Document lastCampo = campoCollection.find().sort(Sorts.descending("_id")).first();
        if (lastCampo != null) {
            String lastCampoId = lastCampo.getObjectId("_id").toString();
            return lastCampoId;
        } else {
            return null;
        }
    }
    
    private void vendimiado(String[] parts) {
        if (parts.length >= 2) {
            String campoId = parts[1];
            Bson filter = Filters.eq("_id", new ObjectId(campoId));
            Bson update = Updates.set("vendimiado", true);
            MongoCollection<Document> campoCollection = database.getCollection("campo");
            UpdateResult updateResult = campoCollection.updateOne(filter, update);
            if (updateResult.getModifiedCount() < 0) {
            	System.out.println("No se han encontrado campos con esta ID.");
            } else {
            	System.out.println("Campo vendimiado correctamente.");
            }
        } else {
            System.out.println("Formato inesperado.");
        }
    }

	private void addBodega(String[] split) {
        b = new Document();
        b.put("nombre", split[1]);
        collection = database.getCollection("Bodega");
        collection.insertOne(b);
    }

	private void getEntrada() {
        collection = database.getCollection("Entrada");
        for (Document doc : collection.find()) {
            this.entradas.add(doc);
        }
    }

	private void showAllCampos() {
        collection = database.getCollection("Campo");
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }
    }
}