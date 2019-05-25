package com.superglue.toweroffense.enemies;

import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.fma.FreeSlotAssignmentStrategy;
import com.badlogic.gdx.ai.fma.SlotAssignmentStrategy;
import com.badlogic.gdx.ai.fma.patterns.DefensiveCircleFormationPattern;
import com.badlogic.gdx.math.Vector2;
import com.superglue.toweroffense.ai.SteeringAgent;

public class DefensiveFormation {
    private static final float DEFEND_RADIUS = 30f;
    SteeringAgent anchor;
    DefensiveCircleFormationPattern<Vector2> defensiveCirclePattern;
    SlotAssignmentStrategy<Vector2> slotAssignmentStrategy;
    Formation<Vector2> formation;

    public DefensiveFormation(float defend_radius, SteeringAgent anchor){
        this.anchor = anchor;
        defensiveCirclePattern = new DefensiveCircleFormationPattern<Vector2>(defend_radius);
        slotAssignmentStrategy = new FreeSlotAssignmentStrategy<Vector2>();
        formation = new Formation(anchor,defensiveCirclePattern, slotAssignmentStrategy );

    }


    public void addToFormation(FormationMember member){
        if (formation != null)
            formation.addMember(member);

         }

    public void removeFromFormation(FormationMember member){
        formation.removeMember(member);
    }

    public void update(){
        if (formation == null)
            return;
        formation.updateSlots();
    }


}
