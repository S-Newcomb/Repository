package com.superglue.toweroffense.collision;

import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class CollisionFilter implements ContactFilter {
    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        return (fixtureA.getFilterData().maskBits & fixtureB.getFilterData().maskBits) != 0;
    }
}
