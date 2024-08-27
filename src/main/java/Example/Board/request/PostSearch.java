package Example.Board.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class PostSearch {

    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

    public PostSearch(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public long getOffset(){
        return (long) (Math.max(1,page) - 1) * Math.max(size, 2000);
    }
}
