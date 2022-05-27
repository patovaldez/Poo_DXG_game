package poo.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

// CLASE PADRE QUE HEREDA DE CLASE Rectangle
public abstract class Object extends Rectangle{

    public Texture image;

    // CONSTRUCTOR
    public Object(int x, int y, int w, int h, Texture img){

        this.x = x;
        this.y = y;
        this.width  = w;
        this.height = h;
        this.image  = img;
    }

    public void speed() {};

}
