package net.terrocidepvp.treetwerk.configurations;

public class Base {

    private int outOf;
    private boolean useParticleEffect;
    private int radiusX;
    private int radiusY;
    private int radiusZ;

    Base(int outOf,
         boolean useParticleEffect,
         int radiusX,
         int radiusY,
         int radiusZ) {
        this.outOf = outOf;
        this.useParticleEffect = useParticleEffect;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.radiusZ = radiusZ;
    }

    public int getOutOf() {
        return outOf;
    }

    public boolean isUseParticleEffect() {
        return useParticleEffect;
    }

    public int getRadiusX() {
        return radiusX;
    }

    public int getRadiusY() {
        return radiusY;
    }

    public int getRadiusZ() {
        return radiusZ;
    }
}
