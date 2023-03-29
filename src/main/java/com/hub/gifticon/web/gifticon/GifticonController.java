package com.hub.gifticon.web.gifticon;

import com.hub.gifticon.domain.gifticon.Gifticon;
import com.hub.gifticon.domain.gifticon.GifticonRepository;
import com.hub.gifticon.domain.gifticon.GifticonService;
import com.hub.gifticon.web.gifticon.form.GifticonAddForm;
import com.hub.gifticon.web.gifticon.form.GifticonUpdateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gifticons")
public class GifticonController {
    private final GifticonService gifticonService;
    private final GifticonRepository gifticonRepository;

    @GetMapping
    public String gifticons(Model model) {
        List<Gifticon> gifticons = gifticonRepository.findAll();
        model.addAttribute("gifticons", gifticons);
        return "gifticons/gifticons";
    }

    @GetMapping("/{gifticonId}")
    public String gifticon(@PathVariable Long gifticonId,
                           Model model) {
        Gifticon gifticon = gifticonRepository.findById(gifticonId);
        model.addAttribute("gifticon", gifticon);
        return "gifticons/gifticon";
    }

    @GetMapping("/add")
    public String addGifticonForm(Model model) {
        model.addAttribute("form", new GifticonAddForm());
        return "gifticons/addGifticonForm";
    }

    @PostMapping("/add")
    public String addGifticon(@Validated @ModelAttribute("form") GifticonAddForm form,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) throws IOException {
        // 실패 로직
        // 바인딩 실패
        if (bindingResult.hasErrors()) {
            return "gifticons/addGifticonForm";
        }
        // 이미지 없음
        if (form.getMultipartFile().isEmpty()) {
            bindingResult.reject("NoFile", "이미지가 없습니다.");
            return "gifticons/addGifticonForm";
        }

        // 성공 로직
        Gifticon addedGifticon = gifticonService.addGifticon(
                form.getGifticonName(),
                form.getExpirationDate(),
                form.getMultipartFile()
        );

        redirectAttributes.addAttribute("gifticonId", addedGifticon.getGifticonId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/gifticons/{gifticonId}";
    }

    @GetMapping("/{gifticonId}/edit")
    public String editGifticonForm(@PathVariable Long gifticonId,
                                   Model model) {
        Gifticon gifticon = gifticonRepository.findById(gifticonId);
        if (gifticon == null) {
            return "gifticons/gifticons";
        }

        GifticonUpdateForm gifticonUpdateForm = new GifticonUpdateForm();
        gifticonUpdateForm.setGifticonName(gifticon.getGifticonName());
        gifticonUpdateForm.setExpirationDate(gifticon.getExpirationDate());

        model.addAttribute("form", gifticonUpdateForm);
        model.addAttribute("gifticonId", gifticonId);
        return "gifticons/editGifticonForm";
    }

    @PostMapping("/{gifticonId}/edit")
    public String editGifticon(@PathVariable Long gifticonId,
                               @Validated @ModelAttribute("form") GifticonUpdateForm form,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) throws IOException {
        // 실패 로직
        // 바인딩 실패
        if (bindingResult.hasErrors()) {
            return "gifticons/editGifticonForm";
        }
        // 이미지 없음
        if (form.getMultipartFile().isEmpty()) {
            bindingResult.reject("NoFile", "이미지가 없습니다.");
            return "gifticons/editGifticonForm";
        }

        // 성공 로직
        Gifticon updatedGifticon = gifticonService.editGifticon(
                gifticonId,
                form.getGifticonName(),
                form.getExpirationDate(),
                form.getMultipartFile()
        );

        // 업데이트 실패
        if (updatedGifticon == null) {
            return "gifticons/gifticons";
        }

        // 업데이트 성공
        redirectAttributes.addAttribute("gifticonId", gifticonId);
        redirectAttributes.addAttribute("status", true);
        return "redirect:/gifticons/{gifticonId}";
    }

    @PostMapping("/{gifticonId}/delete")
    public String deleteGifticon(@PathVariable Long gifticonId) {
        gifticonService.deleteGifticon(gifticonId);
        return "redirect:/gifticons";
    }

    @PostMapping("/{gifticonId}/use")
    public String useGifticon(@PathVariable Long gifticonId) {
        gifticonService.useGifticon(gifticonId);
        return "redirect:/gifticons";
    }
}
