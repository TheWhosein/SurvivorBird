package com.whosein.survivorbird;

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
import com.badlogic.gdx.utils.ScreenUtils;

import org.w3c.dom.Text;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture bird, birdhit;
    Texture enemy1, enemy2, enemy3;
    float birdX = 0 , birdY = 0;
    int gameState = 0;
    float velocity = 0.1f;
    float gravity = 0.1f;
    float enemyVelocity = 2;
    BitmapFont font;
    BitmapFont font2;

    ShapeRenderer shapeRenderer;

    Random random;
    int score = 0;
    int scoreEnemy = 0;

    Circle birdCircle;


    int numberOfEnemies = 4;
    float [] enemyX = new float[numberOfEnemies];

    float [] enemyOffSet = new float[numberOfEnemies];
    float [] enemyOffSet2 = new float[numberOfEnemies];
    float [] enemyOffSet3 = new float[numberOfEnemies];

    Circle [] enemyCircles;
    Circle [] enemyCircles2;
    Circle [] enemyCircles3;



    float distance = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png");
        bird = new Texture("bird.png");
        birdhit = new Texture("birdhit.png");
        enemy1 = new Texture("enemy.png");
        enemy2 =new Texture("enemy.png");
        enemy3 = new Texture("enemy.png");

        birdX = Gdx.graphics.getWidth()/2 - bird.getHeight()/2;
        birdY = Gdx.graphics.getHeight()/3;
        birdCircle = new Circle();

        enemyCircles = new Circle[numberOfEnemies];
        enemyCircles2 = new Circle[numberOfEnemies];
        enemyCircles3 = new Circle[numberOfEnemies];

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(4);

        font2 = new BitmapFont();
        font2.setColor(Color.WHITE);
        font2.getData().setScale(6);
        shapeRenderer = new ShapeRenderer();

        distance = Gdx.graphics.getWidth()/2;

        random = new Random();

        for(int i=0;i<numberOfEnemies;i++){

            enemyOffSet[i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);
            enemyOffSet2[i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);
            enemyOffSet3[i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);

            enemyX[i] = Gdx.graphics.getWidth() - enemy1.getWidth() /2 + i * distance;

            enemyCircles[i] = new Circle();
            enemyCircles2[i] = new Circle();
            enemyCircles3[i] = new Circle();
        }
    }

    @Override
    public void render() {
        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(bird, birdX, birdY, Gdx.graphics.getWidth()/18, Gdx.graphics.getHeight()/12);

        if(gameState == 1){

            if(enemyX[scoreEnemy] < Gdx.graphics.getWidth()/2 - bird.getHeight()/2){
                score++;

                if(scoreEnemy<numberOfEnemies-1){
                    scoreEnemy++;
                }
                else {
                    scoreEnemy = 0;
                }

            }

            if(Gdx.input.justTouched()){
                velocity = -7;
            }

            for(int i=0;i<numberOfEnemies;i++){
                if(enemyX[i] < Gdx.graphics.getWidth()/150){
                    enemyX[i] = enemyX[i] + numberOfEnemies*distance;


                    enemyOffSet[i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);
                    enemyOffSet2[i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);
                    enemyOffSet3[i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);

                }
                else {
                    enemyX[i] = enemyX[i] - enemyVelocity;
                }
                batch.draw(enemy1, enemyX[i], Gdx.graphics.getHeight()/2 + enemyOffSet[i], Gdx.graphics.getWidth()/18, Gdx.graphics.getHeight()/12);
                batch.draw(enemy2, enemyX[i], Gdx.graphics.getHeight()/2 + enemyOffSet2[i], Gdx.graphics.getWidth()/18, Gdx.graphics.getHeight()/12);
                batch.draw(enemy3, enemyX[i], Gdx.graphics.getHeight()/2 + enemyOffSet3[i], Gdx.graphics.getWidth()/18, Gdx.graphics.getHeight()/12);


                enemyCircles[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/36, Gdx.graphics.getHeight()/2 + enemyOffSet[i] +Gdx.graphics.getHeight()/24, Gdx.graphics.getWidth()/36);
                enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/36, Gdx.graphics.getHeight()/2 + enemyOffSet2[i] +Gdx.graphics.getHeight()/24, Gdx.graphics.getWidth()/36);
                enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth()/36, Gdx.graphics.getHeight()/2 + enemyOffSet3[i] +Gdx.graphics.getHeight()/24, Gdx.graphics.getWidth()/36);
            }


            if(birdY>0){
                velocity+=gravity;
                birdY = birdY - velocity;
            }
            else{
                gameState = 2;
            }
        }
        else if(gameState == 0){
            if(Gdx.input.justTouched()){
                gameState = 1;
            }
        }
        else if(gameState == 2){

            font2.draw(batch, "Game Over! Tap To Play Again!", 450, Gdx.graphics.getHeight()/2);

            if(Gdx.input.justTouched()){
                gameState = 1;

                birdY = Gdx.graphics.getHeight()/3;

                for(int i=0;i<numberOfEnemies;i++){

                    enemyOffSet[i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);
                    enemyOffSet2[i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);
                    enemyOffSet3[i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);

                    enemyX[i] = Gdx.graphics.getWidth() - enemy1.getWidth() / 2 + i * distance;

                    enemyCircles[i] = new Circle();
                    enemyCircles2[i] = new Circle();
                    enemyCircles3[i] = new Circle();
                }
                velocity = 0;
                score = 0;
                scoreEnemy = 0;

            }
        }

        font.draw(batch, String.valueOf(score), 100, 200);

        batch.end();

        birdCircle.set(birdX + Gdx.graphics.getWidth()/36, birdY + Gdx.graphics.getHeight()/24, Gdx.graphics.getWidth()/36);

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.BLACK);
        //shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

        for(int i=0;i<numberOfEnemies;i++){
            //shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 36,  Gdx.graphics.getHeight()/2 + enemyOffSet[i] + Gdx.graphics.getHeight() / 24,Gdx.graphics.getWidth() / 36);
            //shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 36,  Gdx.graphics.getHeight()/2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 24,Gdx.graphics.getWidth() / 36);
           // shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 36,  Gdx.graphics.getHeight()/2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 24,Gdx.graphics.getWidth() / 36);
            if(Intersector.overlaps(birdCircle, enemyCircles[i]) || Intersector.overlaps(birdCircle, enemyCircles2[i]) || Intersector.overlaps(birdCircle, enemyCircles3[i])){
                gameState = 2;
            }
        }
        //shapeRenderer.end();

    }

    @Override
    public void dispose() {
    }
}
