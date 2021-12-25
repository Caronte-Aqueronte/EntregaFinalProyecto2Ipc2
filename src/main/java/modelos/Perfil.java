package modelos;

public class Perfil extends Usuario{

    private String descripcion;
    private String hobbies;

    public Perfil(String descripcion, String hobbies, String usuario) {
        super(usuario);
        this.descripcion = descripcion;
        this.hobbies = hobbies;
    }

    public Perfil(String descripcion, String hobbies) {
        this.descripcion = descripcion;
        this.hobbies = hobbies;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    
    
    
}
