
public enum MyGraphEnum {
    SQUARE_LATTICE(1),
    BURST_STRUCTURE(2),
    STAR_STRUCTURE(3),
    SUPERSTAR(4);

    private int typeId;

    public int getTypeId() {
        return typeId;
    }

    MyGraphEnum(int typeId) {
        this.typeId = typeId;
    }


}
