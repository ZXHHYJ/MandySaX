package mandysax.lifecycle;

public interface Factory {
   public <T extends ViewModel> T create(Class<T> modelClass);
}

