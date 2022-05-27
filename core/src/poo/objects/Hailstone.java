package poo.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

// CLASE Hailstone QUE HEREDA DE CLASE Object
public class Hailstone extends Object {

    // CONSTRUCTORES SOBRECARGADOS
    public Hailstone(int x, int y, int w, int h, Texture img){
        super(x, y, w, h, img);
    }

    public Hailstone(int x, int y, Texture img){
        super(x, y, 64, 64, img);
    }

    // SOBRESCRIBIR METODO VIRTUAL DE CLASE Object, PARA SER USADO EN POLIMORFISMO
    @Override
    public void speed(){
        this.y -= 1000 * Gdx.graphics.getDeltaTime();
    }

}
