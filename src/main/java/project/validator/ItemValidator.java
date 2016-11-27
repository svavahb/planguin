package project.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.persistence.entities.ScheduleItem;
import project.service.ScheduleService;

/**
 * Created by Svava on 19.11.16.
 */
public class ItemValidator implements Validator {

    private ScheduleService scheduleService = new ScheduleService();

    @Override
    public boolean supports(Class<?> aClass) {
        return ScheduleItem.class.equals(aClass);
    }

    public void validateItem(Object o, Errors errors, String username) {
        ScheduleItem item = (ScheduleItem) o;

        // Check length of item title
        if (item.getTitle().length() == 0 || item.getTitle().length() > 32) {
            errors.rejectValue("title", "Title can not be empty and length should be less than 32 characters");
        }

        // Check if end time is after start time or not
        boolean retval = scheduleService.checkTime(item.getStartstring(), item.getEndstring());

        if (!retval) {
            errors.rejectValue("endTime", "End Time should be after Start Time");
        }

        // Check whether item overlaps with another item (only if date is empty)
        if (item.getdate() != "") {

            boolean returnvalue = scheduleService.compareTime(item.getStartTime(), item.getEndTime(), username);
            if (!returnvalue) {
                errors.rejectValue("startTime", "overlaps with another item");
            }
        }
    }

    @Override
    public void validate(Object o, Errors errors) {
    }

}
