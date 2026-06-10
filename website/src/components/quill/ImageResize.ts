const HANDLE_SIZE = 12;
const BORDER_WIDTH = 2;

class ImageResize {
  private quill: any;
  private overlay: HTMLDivElement | null = null;
  private activeImg: HTMLImageElement | null = null;

  constructor(quill: any) {
    this.quill = quill;
    quill.root.addEventListener("click", this.onEditorClick);
    window.addEventListener("scroll", this.deselect, true);
  }

  private onEditorClick = (e: MouseEvent) => {
    const target = e.target as HTMLElement;
    if (target.tagName === "IMG" && this.quill.root.contains(target)) {
      this.select(target as HTMLImageElement);
    } else {
      this.deselect();
    }
  };

  private select(img: HTMLImageElement) {
    this.deselect();
    this.activeImg = img;

    const overlay = document.createElement("div");
    overlay.className = "ql-image-resize";
    const rect = img.getBoundingClientRect();
    overlay.style.cssText = [
      "position:fixed",
      `left:${rect.left - BORDER_WIDTH}px`,
      `top:${rect.top - BORDER_WIDTH}px`,
      `width:${rect.width}px`,
      `height:${rect.height}px`,
      "border:2px solid #409eff",
      "cursor:default",
      "pointer-events:none",
      "z-index:1000",
      "box-sizing:content-box",
    ].join(";");

    const handle = document.createElement("div");
    handle.style.cssText = [
      `position:absolute`,
      `right:-${HANDLE_SIZE / 2 + BORDER_WIDTH}px`,
      `bottom:-${HANDLE_SIZE / 2 + BORDER_WIDTH}px`,
      `width:${HANDLE_SIZE}px`,
      `height:${HANDLE_SIZE}px`,
      "background:#409eff",
      "cursor:nwse-resize",
      "pointer-events:auto",
      "border-radius:2px",
    ].join(";");

    overlay.appendChild(handle);
    document.body.appendChild(overlay);
    this.overlay = overlay;

    let startX = 0;
    let startW = 0;

    handle.addEventListener(
      "mousedown",
      (e: MouseEvent) => {
        e.preventDefault();
        e.stopPropagation();
        startX = e.clientX;
        startW = rect.width;

        const onMove = (ev: MouseEvent) => {
          const newW = Math.max(50, startW + ev.clientX - startX);
          overlay.style.width = newW + "px";
        };

        const onUp = () => {
          document.removeEventListener("mousemove", onMove);
          document.removeEventListener("mouseup", onUp);
          const finalW = parseInt(overlay.style.width, 10);
          img.setAttribute("width", String(finalW));
          img.style.width = finalW + "px";
          this.deselect();
        };

        document.addEventListener("mousemove", onMove);
        document.addEventListener("mouseup", onUp);
      },
    );
  }

  private deselect = () => {
    this.activeImg = null;
    if (this.overlay) {
      this.overlay.remove();
      this.overlay = null;
    }
  };
}

export default ImageResize;