package server;

public class Fichier {
    int id;
    String nom;
    byte[] donnee;
    String fichierExtens;
    public Fichier(int id, String name, byte[] data, String fichierExtens) {
        setId(id);
        setName(name);
        setData(data);
        setFichierExtens(fichierExtens);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setName(String nom) {
        this.nom = nom;
    }
    public byte[] getDonnee() {
        return donnee;
    }
    public void setData(byte[] atae) {
        this.data = data;
    }
    public String getFichierExtens() {
        return fichierExtens;
    }
    public void setFichierExtens(String fichierExtens) {
        this.fichierExtens = fichierExtens;
    }
}
