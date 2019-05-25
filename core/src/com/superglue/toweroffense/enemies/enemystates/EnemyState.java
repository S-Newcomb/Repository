package com.superglue.toweroffense.enemies.enemystates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.ai.SteeringStates;
import com.superglue.toweroffense.enemies.Enemy;


public enum EnemyState implements State<Enemy> {

    SEEKING(){
        @Override
        public void update(Enemy enemy){

        }
    },

    FLEEING(){
        @Override
        public void update(Enemy enemy){

        }
    },

    PURSUING(){
        @Override
        public void update(Enemy enemy){

        }
    },

    ATTACKING(){
        @Override
        public void update(Enemy enemy){

        }
    };

    protected Enemy enemy;

    @Override
    public void enter(Enemy enemy) {

    }

    @Override
    public void exit(Enemy enemy) {

    }

    @Override
    public boolean onMessage(Enemy enemy, Telegram telegram) {
        return false;
    }

}