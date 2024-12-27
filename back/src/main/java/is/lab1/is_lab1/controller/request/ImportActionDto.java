package is.lab1.is_lab1.controller.request;

import java.time.format.DateTimeFormatter;
import java.util.List;

import is.lab1.is_lab1.model.Car;
import is.lab1.is_lab1.model.Coordinates;
import is.lab1.is_lab1.model.HumanBeing;
import is.lab1.is_lab1.model.ImportAction;
import is.lab1.is_lab1.model.ObjectType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImportActionDto {

    private Long id;

    private ObjectType importActionType;

    private List<HumanBeing> humanBeings;

    private List<Car> cars;

    private List<Coordinates> coordinates;

    private String creationDate;

    private String isUser;

    private Integer count;

    public ImportActionDto(ImportAction action) {
        this.id = action.getId();
        this.importActionType = action.getImportActionType();
        this.humanBeings = action.getHumanBeings();
        this.coordinates = action.getCoordinates();
        this.creationDate = action.getCreationDate().format(DateTimeFormatter.ofPattern("d MMM uuuu"));
        this.cars = action.getCars();
        this.isUser = action.getIsUser().getUsername();
        this.count = action.getCount();
    }

}