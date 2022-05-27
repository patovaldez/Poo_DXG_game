package poo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import poo.objects.Object;
import poo.objects.Player;
import poo.objects.Drop;
import poo.objects.Hailstone;

public class PooGame extends ApplicationAdapter {


	private Texture dropWaterImage;
	private Texture dropOilImage;
	private Texture bucketImage;
	private Texture hailstoneImage;
	private Sound 	dropSound;
	private Sound hitSound;
	private Music 	rainMusic;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Player bucket;				// BUCKET DEL TIPO CLASE Player
	private Array<Object> rainDrops;	// VECTOR DE GOTAS DE OBJETOS DE LA CLASE PADRE
	private long lastDropTime;
	private int score = 0;
	private int lives = 3;
	
	@Override
	public void create () {

		// CARGADO DE IMAGENES
		dropWaterImage = new Texture(Gdx.files.internal("drop_water.png"));
		dropOilImage = new Texture(Gdx.files.internal("drop_oil.png"));
		hailstoneImage = new Texture(Gdx.files.internal("hailstone.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		// CARGA EFECTOS DE SONIDOS Y MÚSICA DE FONDO
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		hitSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// INICIA SONIDO DE FONDO
		rainMusic.setLooping(true);
		rainMusic.play();

		// INICIALIZACIÓN DE CAMERA Y SPRITEBATCH
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();

		// INSTANCIAMOS LA IMAGEN DE LA CUBETA EN EL JUEGO USANDO UN RECTÁNGULO
		bucket	  = new Player(800/2 - 64/2, 20, bucketImage);
		rainDrops = new Array<Object>();

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();

		// RENDERIZADO DE IMAGENES
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bucket.image, bucket.x, bucket.y);

		for(Object raindrop: rainDrops) {
			batch.draw(raindrop.image, raindrop.x, raindrop.y);
		}
		batch.end();

		// DETECTA EVENTO DEL MOUSE Y AFECTA POSICIÓN DEL PLAYER
		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}

		// DETECTA EVENTO DE TECLADO Y AFECTA POSICIÓN DEL PLAYER
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// EVITA QUE LA IMAGEN DEL PLAYER SALGA DEL ÁREA DE JUEGO
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;

		// CADA SEGUNDO LLAMA MÉTODO DE UTILIDAD PARA GNERAR NUEVAS GOTAS
		if(System.currentTimeMillis() - lastDropTime > 1000) {
			spawnRaindrop();
			System.out.println("Elementos: " + rainDrops.size);
		}

		// RECORRE ARREGLO DE GOTAS, DETERMINA SU AVANCE MEDIANTE POLIMORFISMO, SI LLEGA AL FINAL DE PANTALLA Y SU COLISIÓN CON PLAYER
		for (Array.ArrayIterator<Object> iter = rainDrops.iterator(); iter.hasNext(); ) {
			Object raindrop = iter.next();
			// MÉTODO PARA COMPORTAMIENTO POLIMÓRFICO
			raindrop.speed();
			// DETECTA SI LA GOTA LLEGA AL SUELO
			if(raindrop.y + 64 < 0) {
				hitSound.play();
				iter.remove();
				lives--;
				System.out.println("Vidas: " + lives);
			}
			// DETECTA COLISIÓN CON PLAYER
			if(raindrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
				score++;
				System.out.println("Score: " + score);
			}
		}

		// TERMINA EL JUEGO EN CASO DE PERDER TODAS LAS VIDAS
		if(lives == 0) {
			Gdx.app.exit();
			System.exit(0);
		}

	}
	
	@Override
	public void dispose () {
		dropWaterImage.dispose();
		dropOilImage.dispose();
		hailstoneImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		hitSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}

	// MÉTODO DE UTILIDAD PARA GENERAR LAS GOTAS EN EL ESCENARIO
	private void spawnRaindrop() {
		Object rain;

		if(MathUtils.random(0, 3) == 0) {
			// GENERA GRANIZO CON 25% DE PROBABILIDAD
			rain = new Hailstone(MathUtils.random(0, 800-64),480, hailstoneImage);
		}
		else{
			// GENERA OBJETO GOTA CON 75% DE PROBABILIDAD, CON VARIANTE DE TEXTURA: GOTA DE ACEITE O DE AGUA (CON 50% DE PROBABILIDAD)
			rain = new Drop(MathUtils.random(0, 800-64),480, MathUtils.random(0, 1)==0?dropOilImage :dropWaterImage);
		}

		rainDrops.add(rain);
		lastDropTime = System.currentTimeMillis();
	}
}
