package com.adhiraj.fappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
//	ShapeRenderer shapeRenderer ;

	Texture bg , bgstart , gameover;
	Texture [] Birds ;
	Texture  toptube , bottomtube ;

	int birdno = 0 , fly = 0 , gravity = 100 , gamestate =0 , birdheight = 0 , gap =250 ,tubeno =4 , scoringTube , score ,lastScore = 0;
	BitmapFont font , prevscore;

	Circle birdcircle ;
	Rectangle[] topTubeRectangle , bottomTubeRectangle;

	float maxtubeoffset , tubevelocity = 15 , tubedistance;
	float [] tubex = new float[4] ,tubeoffset = new float[4];
	Random randomGenerator ;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
//		shapeRenderer = new ShapeRenderer();
		birdcircle = new Circle();
		topTubeRectangle = new  Rectangle[tubeno];
		bottomTubeRectangle = new  Rectangle[tubeno];

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		prevscore = new BitmapFont();
		prevscore.getData().setScale(10);

		bg = new Texture("background-night.png");
		bgstart = new Texture("gamescreen.png");
		toptube = new Texture("toptube.png");
		bottomtube = new Texture("bottomtube.png");
		gameover = new Texture("gameover.png");


		Birds = new Texture[4];
		Birds[0] = new Texture("bird1.png");
		Birds[1] = new Texture("bird2.png");
		Birds[2] = new Texture("bird3.png");
		Birds[3] = new Texture("bird4.png");



		maxtubeoffset = Gdx.graphics.getHeight()/2 -gap/2 -100 ;
		randomGenerator = new Random();

		tubedistance = Gdx.graphics.getWidth()/2 + 100;

        startGame();

	}

	public void startGame(){
		fly = 0;
		gravity = 100;
		score = 0 ;
		lastScore = 0;
		scoringTube = 0 ;
		birdheight = (Gdx.graphics.getHeight() / 2 - Birds[birdno].getHeight() / 3) + fly;
		for(int i =0; i<tubeno; i++){
			tubeoffset[i] = (randomGenerator.nextFloat() - 0.4f) * 780;
			tubex[i] = Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth() +i*tubedistance;
			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();

		if(gamestate == 0){
			batch.draw(bgstart , -2 , -1 , Gdx.graphics.getWidth()+10, Gdx.graphics.getHeight()+20);
			if(Gdx.input.justTouched()){
				gamestate = 1 ;
			}
		}
		else if(gamestate == 1) {
			batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			if(tubex[scoringTube] < (Gdx.graphics.getWidth()/2)-200){
				score++;
				lastScore++;
				Gdx.app.log("Score", String.valueOf(score));
				if(scoringTube<tubeno-1){
					scoringTube++;
				}else{
					scoringTube = 0;
				}
			}

        for(int i =0 ; i<tubeno ; i++){
        	if(tubex[i] < -toptube.getWidth()){
        		tubex[i] += tubeno*tubedistance;
			tubeoffset[i] = (randomGenerator.nextFloat() - 0.4f) * 780;
			}else{
				tubex[i] -= tubevelocity;
			}
			batch.draw(toptube , tubex[i],Gdx.graphics.getHeight()/2 + gap + tubeoffset[i]);
			batch.draw(bottomtube , tubex[i],Gdx.graphics.getHeight()/2 - gap -bottomtube.getHeight() + tubeoffset[i]);
			topTubeRectangle[i] = new Rectangle(tubex[i],Gdx.graphics.getHeight()/2 + gap + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
			bottomTubeRectangle[i] = new Rectangle(tubex[i],Gdx.graphics.getHeight()/2 - gap -bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());
		}


			birdheight = (Gdx.graphics.getHeight() / 2 - Birds[birdno].getHeight() / 3) + fly;
			if(birdheight<0){
				birdheight = 0;
				fly = -(Gdx.graphics.getHeight() / 2 - Birds[birdno].getHeight() / 3);
				gravity = 100 ;
				gamestate = 2;

			}else if(birdheight > Gdx.graphics.getHeight()-200){
				birdheight = Gdx.graphics.getHeight();
				fly = (Gdx.graphics.getHeight() / 2 - Birds[birdno].getHeight() / 3)+85;
				gravity = 100 ;
			}

			if (Gdx.input.isTouched()) {


				fly += 50;
				gravity = 40 ;
			} else {
				gravity+=5;
				fly -= gravity;
			}

			if (birdno == Birds.length - 1) {
				birdno = 0;
			} else {
				birdno++;
			}
			try {

				Thread.sleep(80);
			} catch (Exception e) {
				e.printStackTrace();
			}

			batch.draw(Birds[birdno], Gdx.graphics.getWidth() / 2 - Birds[birdno].getWidth() / 3,birdheight , Birds[birdno].getWidth() / 4, Birds[birdno].getHeight() / 4);
			font.draw(batch, "Score "+String.valueOf(score) , Gdx.graphics.getWidth()/2 - 200, Gdx.graphics.getHeight()/(1.1f));

			birdcircle.set(Gdx.graphics.getWidth()/2  - Birds[birdno].getWidth() / 3+100, birdheight+80,Birds[birdno].getWidth()/8);
			for(int i =0 ; i<tubeno ; i++){
				if(Intersector.overlaps(birdcircle,topTubeRectangle[i]) || Intersector.overlaps(birdcircle,bottomTubeRectangle[i]) ){
					gamestate = 2;
				}
			}

		}
		else if(gamestate == 2){

			prevscore.setColor(Color.WHITE);
			batch.draw(gameover ,-2 , -1 , Gdx.graphics.getWidth()+10, Gdx.graphics.getHeight()+20);
            prevscore.draw(batch, "Score: "+String.valueOf(lastScore) , Gdx.graphics.getWidth()/2 - 300, Gdx.graphics.getHeight()/(1.35f));
			if(Gdx.input.justTouched()){
				gamestate = 1 ;
				startGame();
				prevscore.setColor(Color.CLEAR);
			}
		}
		batch.end();




//		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(birdcircle.x,birdcircle.y,birdcircle.radius);

//		for(int i =0 ; i<tubeno ; i++){
//			shapeRenderer.rect(tubex[i],Gdx.graphics.getHeight()/2 + gap + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
//			shapeRenderer.rect(tubex[i],Gdx.graphics.getHeight()/2 - gap -bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());
//			if(Intersector.overlaps(birdcircle,topTubeRectangle[i]) || Intersector.overlaps(birdcircle,bottomTubeRectangle[i]) ){
//				gamestate = 2;
//			}
//		}

//		shapeRenderer.end();


	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
