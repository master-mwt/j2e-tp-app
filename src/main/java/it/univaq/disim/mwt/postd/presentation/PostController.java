package it.univaq.disim.mwt.postd.presentation;

import it.univaq.disim.mwt.postd.business.*;
import it.univaq.disim.mwt.postd.domain.ChannelClass;
import it.univaq.disim.mwt.postd.domain.PostClass;
import it.univaq.disim.mwt.postd.domain.UserChannelRole;
import it.univaq.disim.mwt.postd.domain.UserClass;
import it.univaq.disim.mwt.postd.helpers.TemplateHelper;
import it.univaq.disim.mwt.postd.utils.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("posts")
public class PostController {

    @Autowired
    private PostBO postBO;
    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private TagBO tagBO;

    @Autowired
    private UtilsClass utilsClass;

    @Autowired
    private TemplateHelper templateHelper;

    @PostMapping("create")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.postd.domain.ChannelClass', 'create_post')")
    public String save(@Valid @ModelAttribute("post") PostClass post, BindingResult bindingResult, @RequestParam("tags") String tags, @RequestParam("files") MultipartFile[] images, @RequestParam("channelId") Long channelId, Model model) throws BusinessException {

        if(bindingResult.hasErrors()) {
            ChannelClass channel = channelBO.findById(post.getChannelId());
            Page<PostClass> postPage = postBO.findByChannelIdOrderByCreatedAtDescPaginated(post.getChannelId(), 0, 10);
            model.addAttribute("post", post);
            model.addAttribute("channel", channel);
            model.addAttribute("postPage", postPage);
            model.addAttribute("templateHelper", templateHelper);
            UserClass principal = utilsClass.getPrincipal();
            model.addAttribute("principal", principal);
            UserChannelRole subscription = utilsClass.getSubscription(channel, principal);
            model.addAttribute("subscription", subscription);
            model.addAttribute("tags", tagBO.findAll());
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "pages/discover/channel";
        }
        postBO.createPostInChannel(post, tags, images);

        return "redirect:/discover/post/" + post.getId();
    }
}
