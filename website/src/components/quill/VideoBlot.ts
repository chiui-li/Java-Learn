import Quill from "quill";

const BlockEmbed = Quill.import("blots/block/embed");

export interface VideoBlotValue {
  url: string;
  width?: string;
  poster?: string;
}

class VideoBlot extends BlockEmbed {
  static blotName = "video";
  static tagName = "video";

  static create(value: string | VideoBlotValue): HTMLElement {
    const node = super.create();
    const src = typeof value === "string" ? value : value.url;
    node.setAttribute("src", src);
    node.setAttribute("controls", "true");
    node.setAttribute("loading", "lazy");
    if (typeof value !== "string" && value.width) {
      node.setAttribute("width", value.width);
    }
    if (typeof value !== "string" && value.poster) {
      node.setAttribute("poster", value.poster);
    }
    return node;
  }

  static value(domNode: HTMLElement): VideoBlotValue {
    return {
      url: domNode.getAttribute("src") || "",
      width: domNode.getAttribute("width") || undefined,
      poster: domNode.getAttribute("poster") || undefined,
    };
  }

  format(name: string, value: any) {
    if (name === "width") {
      this.domNode.setAttribute("width", value);
    } else {
      super.format(name, value);
    }
  }
}

export default VideoBlot;
