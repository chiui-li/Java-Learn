import Quill from "quill";

const BlockEmbed = Quill.import("blots/block/embed");

export interface ImageBlotValue {
  url: string;
  width?: string;
}

class ImageBlot extends BlockEmbed {
  static blotName = "image";
  static tagName = "img";

  static create(value: string | ImageBlotValue): HTMLElement {
    const node = super.create();
    const src = typeof value === "string" ? value : value.url;
    node.setAttribute("src", src);
    node.setAttribute("loading", "lazy");
    if (typeof value !== "string" && value.width) {
      node.setAttribute("width", value.width);
    }
    // s.appendChild(node);
    return node;
  }

  static value(domNode: HTMLElement): ImageBlotValue {
    return {
      url: domNode.getAttribute("src") || "",
      width: domNode.getAttribute("width") || undefined,
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

export default ImageBlot;
