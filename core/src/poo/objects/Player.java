package poo.objects;

import com.badlogic.gdx.graphics.Texture;

// CLASE Player QUE HEREDA DE CLASE Object
public class Player extends Object {

    // CONSTRUCTORES SOBRECARGADOS
    public Player(int x, int y, int w, int h, Texture img){
        super(x, y, w, h, img);
        }

    public Player(int x, int y, Texture img){
        super(x, y, 64, 64, img);
    }

}
