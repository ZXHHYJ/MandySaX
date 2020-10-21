package mandysax.lifecycle;

public abstract interface Factory
{
    public abstract <T extends ViewModel> T create(Class<T> modelClass);
}
